import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IcedragonSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, LacksAuthorityDirective } from './';

@NgModule({
  imports: [IcedragonSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, LacksAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [IcedragonSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, LacksAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcedragonSharedModule {
  static forRoot() {
    return {
      ngModule: IcedragonSharedModule
    };
  }
}
