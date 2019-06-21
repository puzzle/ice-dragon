import { NgModule } from '@angular/core';

import { IcedragonSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [IcedragonSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [IcedragonSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class IcedragonSharedCommonModule {}
