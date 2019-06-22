import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IcedragonSharedModule } from 'app/shared';
import { DASHBOARD_ROUTE, DashboardComponent } from './';

@NgModule({
  imports: [IcedragonSharedModule, RouterModule.forChild([DASHBOARD_ROUTE])],
  declarations: [DashboardComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcedragonDashboardModule {}
