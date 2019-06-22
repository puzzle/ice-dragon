import { Component, OnInit } from '@angular/core';
import { Platform } from 'app/icedragon/platform.model';
import { AccountService } from 'app/core';
import { Subscription } from 'app/icedragon/subscription.model';
import { IcedragonService } from 'app/icedragon/icedragon.service';

@Component({
  selector: 'jhi-dasboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['dashboard.scss']
})
export class DashboardComponent implements OnInit {
  platforms: Platform[];
  subscriptions: Subscription[];

  constructor(private accountService: AccountService, private iceDragonService: IcedragonService) {}

  ngOnInit() {
    this.loadAccount(false);
  }

  getSubscriptionSorter() {
    return (s1: Subscription, s2: Subscription) => {
      if (s1.active !== s2.active) {
        if (s1.active === true) {
          return -1;
        } else {
          return 1;
        }
      }
      return (s2.validFrom || '0').localeCompare(s1.validFrom || '0');
    };
  }

  redeemSatoshis(platform: Platform) {
    platform.redeeming = true;
    this.iceDragonService.redeemGainz(platform).subscribe(() => {
      this.loadAccount(true);
      platform.redeeming = false;
    });
  }

  loadAccount(force: boolean) {
    this.accountService.identity(force).then(account => {
      this.platforms = account.platforms.sort((a, b) => a.id - b.id);
      this.subscriptions = account.subscriptions.sort(this.getSubscriptionSorter());
    });
  }
}
