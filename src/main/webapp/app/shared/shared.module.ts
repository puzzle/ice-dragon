import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IcedragonSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [IcedragonSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [IcedragonSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcedragonSharedModule {
  static forRoot() {
    return {
      ngModule: IcedragonSharedModule
    };
  }
}
