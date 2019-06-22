import { Route } from '@angular/router';

import { ProvideComponent } from './';

export const PROVIDE_ROUTE: Route = {
  path: 'provide',
  component: ProvideComponent,
  data: {
    authorities: [],
    pageTitle: 'Ice Dragon - tears down pay walls and liberates the web from advertisement'
  }
};
