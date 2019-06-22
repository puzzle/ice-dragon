import { Component, OnInit } from '@angular/core';
import { IcedragonService } from 'app/icedragon/icedragon.service';
import { Platform } from 'app/icedragon/platform.model';
import { AccountService } from 'app/core';
import { Subscription } from 'app/icedragon/subscription.model';

@Component({
  selector: 'jhi-dasboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['dashboard.scss']
})
export class DashboardComponent implements OnInit {
  private platforms: Platform[];
  private subscriptions: Subscription[];

  constructor(private accountService: AccountService) {}

  ngOnInit() {
    this.accountService.identity(false).then(account => {
      this.platforms = account.platforms;
      this.subscriptions = account.subscriptions.sort(this.getSubscriptionSorter());
    });
  }

  private getSubscriptionSorter() {
    return (s1: Subscription, s2: Subscription) => {
      if (s1.active !== s2.active) {
        if (s1.active === true) {
          return -1;
        } else {
          return 1;
        }
      }
      return s2.validFrom.diff(s1.validFrom);
    };
  }
}
