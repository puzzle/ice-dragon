import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, AccountService, AuthServerProvider, JhiTrackerService, LoginModalService } from 'app/core';

import { GetInfoResponse, requestProvider, WebLNProvider } from 'webln';
import { IcedragonService } from 'app/icedragon/icedragon.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  account: Account;
  modalRef: NgbModalRef;
  webLN: WebLNProvider;
  info: GetInfoResponse;

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private service: IcedragonService,
    private authService: AuthServerProvider,
    private trackerService: JhiTrackerService
  ) {}

  ngOnInit() {
    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
    this.registerWebLN();
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
      });
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
          this.accountService.identity(true).then(account => {
            this.trackerService.sendActivity();
            this.account = account;
          });
        });
      });
    });
  }
}
