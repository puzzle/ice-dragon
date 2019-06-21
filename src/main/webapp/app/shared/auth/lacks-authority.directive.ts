import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';

@Directive({
  selector: '[jhiLacksAuthority]'
})
export class LacksAuthorityDirective {
  private authorities: string[];

  constructor(private accountService: AccountService, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

  @Input()
  set jhiLacksAuthority(value: string | string[]) {
    this.authorities = typeof value === 'string' ? [value] : value;
    this.updateView();
    // Get notified each time authentication state changes.
    this.accountService.getAuthenticationState().subscribe(() => this.updateView());
  }

  private updateView(): void {
    const lacksAuthority = !this.accountService.hasAnyAuthority(this.authorities);
    this.viewContainerRef.clear();
    if (lacksAuthority) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    }
  }
}
