import { Route } from '@angular/router';

import { DashboardComponent } from './';

export const DASHBOARD_ROUTE: Route = {
  path: 'dashboard',
  component: DashboardComponent,
  data: {
    authorities: [],
    pageTitle: 'Ice Dragon - tears down pay walls and liberates the web from advertisement'
  }
};
