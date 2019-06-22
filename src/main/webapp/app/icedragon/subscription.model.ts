import { Platform } from 'app/icedragon/platform.model';

export interface Subscription {
  id?: number;
  validFrom?: string;
  duration?: number;
  paymentHash?: string;
  preImage?: string;
  invoiceString?: string;
  platform?: Platform;
  active?: boolean;
  platformId?: number;
}

export function random(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}
