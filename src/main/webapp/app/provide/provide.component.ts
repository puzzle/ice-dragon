import { AfterViewInit, Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PLATFORM_NAME_EXISTS_TYPE, PLATFORM_URL_EXISTS_TYPE } from 'app/shared';
import { IcedragonService } from 'app/icedragon/icedragon.service';

export const URL_PATTERN = /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}(\.[a-z]{2,6})?\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/;
export const PLACEHOLDER_PATTERN = /<<<YOUR_PAYMENT_CONFIRMATION_SECRET>>>/g;
export const PHP_FILE_1_TEMPLATE = require('./dragons-nest-1.template.txt').default;
export const PHP_FILE_2_TEMPLATE = require('./dragons-nest-2.template.txt').default;

@Component({
  selector: 'jhi-provide',
  templateUrl: './provide.component.html',
  styleUrls: ['provide.scss']
})
export class ProvideComponent implements OnInit, AfterViewInit {
  error: string;
  errorNameExists: string;
  errorUrlExists: string;
  success: boolean;
  script1: string = PHP_FILE_1_TEMPLATE;
  script2: string;

  registerForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(255), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    amountPerHour: ['100', [Validators.required, Validators.min(1), Validators.max(100000000)]],
    serviceUrl: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255), Validators.pattern(URL_PATTERN)]],
    contentUrl: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255), Validators.pattern(URL_PATTERN)]]
  });

  constructor(
    private icedragonService: IcedragonService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.success = false;
  }

  ngAfterViewInit() {
    this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#name'), 'focus', []);
  }

  register() {
    const platform = this.registerForm.value;

    this.icedragonService.addPlatform(platform).subscribe(
      code => {
        this.success = true;
        this.script2 = PHP_FILE_2_TEMPLATE.replace(PLACEHOLDER_PATTERN, code);
      },
      response => this.processError(response)
    );
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === PLATFORM_NAME_EXISTS_TYPE) {
      this.errorNameExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === PLATFORM_URL_EXISTS_TYPE) {
      this.errorUrlExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }
}
