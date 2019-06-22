export interface Platform {
  id: number;
  name: string;
  amountPerHour: number;
  serviceUrl: string;
  contentUrl: string;
  earnedSatoshis: number;
  payedOutSatoshis: number;
}
