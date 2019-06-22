import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IcedragonSharedModule } from 'app/shared';
import { PROVIDE_ROUTE, ProvideComponent } from './';

@NgModule({
  imports: [IcedragonSharedModule, RouterModule.forChild([PROVIDE_ROUTE])],
  declarations: [ProvideComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcedragonProvideModule {}
