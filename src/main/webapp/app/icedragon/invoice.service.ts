import { Injectable } from '@angular/core';
import { Observable, Observer, Subscription as RxSubscription } from 'rxjs';
import { getServerUrl, getWebsocketUrl } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { WindowRef } from 'app/core/tracker/window.service';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  stompClient = null;
  subscriber = null;
  connection: Promise<any>;
  connectedPromise: any;
  listener: Observable<any>;
  listenerObserver: Observer<any>;

  private subscription: RxSubscription;

  constructor(private http: HttpClient, private $window: WindowRef) {
    this.connection = this.createConnection();
    this.listener = this.createListener();
  }

  connect(errCb) {
    if (this.connectedPromise === null) {
      this.connection = this.createConnection();
    }
    // building absolute path so that websocket doesn't fail when deploying with a context path
    const socket = new SockJS(getWebsocketUrl(this.$window) + 'invoice?access_token=');
    this.stompClient = Stomp.over(socket);
    const headers = {};
    this.stompClient.connect(
      headers,
      () => {
        this.connectedPromise('success');
        this.connectedPromise = null;
      },
      errCb
    );
  }

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
      this.stompClient = null;
    }
    if (this.subscription) {
      this.subscription.unsubscribe();
      this.subscription = null;
    }
  }

  receive() {
    return this.listener;
  }

  subscribe() {
    this.connection.then(() => {
      this.subscriber = this.stompClient.subscribe('/topic/invoice', data => {
        this.listenerObserver.next(JSON.parse(data.body));
      });
    });
  }

  unsubscribe() {
    if (this.subscriber !== null) {
      this.subscriber.unsubscribe();
    }
    this.listener = this.createListener();
  }

  private createListener(): Observable<any> {
    return new Observable(observer => {
      this.listenerObserver = observer;
    });
  }

  private createConnection(): Promise<any> {
    return new Promise((resolve, reject) => (this.connectedPromise = resolve));
  }
}
