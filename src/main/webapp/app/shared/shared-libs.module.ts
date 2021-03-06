import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { PrismModule } from '@ngx-prism/core';

@NgModule({
  imports: [NgbModule, InfiniteScrollModule, CookieModule.forRoot(), FontAwesomeModule, ReactiveFormsModule, PrismModule],
  exports: [FormsModule, CommonModule, NgbModule, NgJhipsterModule, InfiniteScrollModule, FontAwesomeModule, ReactiveFormsModule]
})
export class IcedragonSharedLibsModule {
  static forRoot() {
    return {
      ngModule: IcedragonSharedLibsModule
    };
  }
}
