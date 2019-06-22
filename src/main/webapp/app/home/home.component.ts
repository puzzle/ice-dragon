import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import * as lodash from 'lodash';

import { Account, AccountService, AuthServerProvider, LoginModalService } from 'app/core';

import { GetInfoResponse, requestProvider, WebLNProvider } from 'webln';
import { IcedragonService } from 'app/icedragon/icedragon.service';
import { Platform } from 'app/icedragon/platform.model';
import { SubscribeDialogService } from 'app/home/subscribe-dialog.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  account: Account;
  modalRef: NgbModalRef;
  webLN: WebLNProvider;
  info: GetInfoResponse;
  platforms: Platform[];

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private service: IcedragonService,
    private authService: AuthServerProvider,
    private subscribeDialog: SubscribeDialogService
  ) {}

  ngOnInit() {
    this.checkLogin();
    this.registerAuthenticationSuccess();
    this.registerWebLN();
  }

  ngAfterViewInit(): void {
    this.checkLogin();
  }

  checkLogin() {
    this.accountService.identity(true).then((account: Account) => {
      this.account = account;
      this.getPlatforms();
    });
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', () => {
      this.checkLogin();
    });
  }

  registerWebLN() {
    requestProvider().then(provider => {
      this.webLN = provider;
      this.webLN.getInfo().then(info => (this.info = info));
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  loginReckless() {
    this.service.getChallenge(this.info.node.pubkey).subscribe(challenge => {
      this.webLN.signMessage(challenge).then(signResponse => {
        this.service.loginReckless(this.info.node.pubkey, signResponse.signature).subscribe(token => {
          this.authService.storeAuthenticationToken(token.id_token, false);
          this.checkLogin();
        });
      });
    });
  }

  getPlatforms() {
    if (this.accountService.isAuthenticated()) {
      this.service.getPlatforms().subscribe(platforms => (this.platforms = platforms));
    }
  }

  subscribe(platform: Platform) {
    this.subscribeDialog.openDialog(platform).result.then(() => this.checkLogin());
  }

  hasSubscription(platform: Platform) {
    if (this.platforms && this.account && this.account.subscriptions) {
      return lodash.some(this.account.subscriptions, s => s.platformId === platform.id);
    }
    return false;
  }
}
