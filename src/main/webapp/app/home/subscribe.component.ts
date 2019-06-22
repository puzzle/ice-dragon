import { Component, ElementRef, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { flash } from 'light-it-up';
import * as $ from 'jquery';
import { DomSanitizer } from '@angular/platform-browser';
import { finalize } from 'rxjs/internal/operators';
import { Subscription } from '../icedragon/subscription.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../icedragon/invoice.service';
import { Platform } from 'app/icedragon/platform.model';
import { IcedragonService } from 'app/icedragon/icedragon.service';
import { requestProvider, WebLNProvider } from 'webln';

export const SUCCESS_FLASH_DURATION = 2000;

@Component({
  selector: 'jhi-subscribe-modal',
  templateUrl: './subscribe.component.html',
  styleUrls: ['subscribe.component.scss']
})
export class JhiSubscribeModalComponent implements OnInit {
  public platform: Platform;

  invoice: Subscription;
  durationHours = 1;
  loading: boolean;
  checkoutError: string;
  touched: boolean;
  paid = false;
  webLN: WebLNProvider;

  constructor(
    private eventManager: JhiEventManager,
    private elementRef: ElementRef,
    private invoiceService: InvoiceService,
    public activeModal: NgbActiveModal,
    public sanitizer: DomSanitizer,
    private service: IcedragonService
  ) {}

  ngOnInit(): void {
    this.registerWebLN();
  }

  registerWebLN() {
    requestProvider().then(provider => {
      this.webLN = provider;
    });
  }

  get paymentLink() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`lightning:${this.invoice.invoiceString}`);
  }

  checkout() {
    this.loading = true;
    this.checkoutError = null;
    this.service
      .addSubscription(this.platform, this.durationHours)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe(
        (invoice: Subscription) => {
          this.invoice = invoice;
          this.invoiceService.subscribe();
          this.invoiceService.receive().subscribe(val => {
            if (val.id === this.invoice.id && val.active === true) {
              this.service.getToken(this.platform).subscribe(token => {
                this.service.pushTokenToService(token, this.platform.serviceUrl).subscribe(response => {
                  if (response === 'success') {
                    this.invoicePaid();
                  } else {
                    throw new Error('Something went wrong, the content provider probably configured their page wrong.');
                  }
                });
              });
            }
          });
          if (this.webLN) {
            this.webLN.sendPayment(invoice.invoiceString);
          }
        },
        err => (this.checkoutError = err.error.message || err.error.title)
      );
  }

  invoicePaid() {
    flash({
      element: $('.modal-content')[0],
      colorFlash: '#6cb02c',
      zIndex: 9999,
      duration: SUCCESS_FLASH_DURATION
    });
    setTimeout(() => {
      this.paid = true;
    }, SUCCESS_FLASH_DURATION);
  }

  close() {
    this.activeModal.close(this.invoice);
  }
}
