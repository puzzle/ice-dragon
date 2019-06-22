import { Moment } from 'moment';

export interface Subscription {
  id?: number;
  validFrom?: Moment;
  duration?: number;
  paymentHash?: string;
  preImage?: string;
  invoiceString?: string;
}

export function random(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}
