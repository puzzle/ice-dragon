import { RequestInvoiceArgs } from 'webln/lib/provider';

export interface Platform {
  redeeming?: boolean;
  id: number;
  name: string;
  amountPerHour: number;
  serviceUrl: string;
  contentUrl: string;
  earnedSatoshis: number;
  payedOutSatoshis: number;
  invoice?: string;
  paymentConfirmationSecret?: string;
}
