import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IcedragonSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { JhiSubscribeModalComponent } from 'app/home/subscribe.component';
import { QRCodeModule } from 'angularx-qrcode';

@NgModule({
  imports: [IcedragonSharedModule, RouterModule.forChild([HOME_ROUTE]), QRCodeModule],
  declarations: [HomeComponent, JhiSubscribeModalComponent],
  entryComponents: [JhiSubscribeModalComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcedragonHomeModule {}
