import { Subscription } from 'app/icedragon/subscription.model';
import { Platform } from 'app/icedragon/platform.model';

export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string,
    public langKey: string,
    public lastName: string,
    public login: string,
    public imageUrl: string,
    public subscriptions: Subscription[],
    public platforms: Platform[]
  ) {}
}
