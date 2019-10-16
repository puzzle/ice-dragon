import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { Platform } from 'app/icedragon/platform.model';
import { Subscription } from 'app/icedragon/subscription.model';

@Injectable({ providedIn: 'root' })
export class IcedragonService {
  constructor(private http: HttpClient) {}

  getChallenge(pubKey: string): Observable<string> {
    return this.http.post(SERVER_API_URL + 'api/recklessuser/challenge', { nodePublicKey: pubKey }, { responseType: 'text' });
  }

  loginReckless(pubKey: string, signature: string): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/recklessuser/login', { nodePublicKey: pubKey, challengeResponse: signature });
  }

  addPlatform(platform: Platform): Observable<string> {
    return this.http.post(SERVER_API_URL + 'api/platform', platform, { responseType: 'text' });
  }

  getPlatforms(): Observable<Platform[]> {
    return this.http.get<Platform[]>(SERVER_API_URL + 'api/platform');
  }

  addSubscription(platform: Platform, duration: number): Observable<Subscription> {
    return this.http.post<Subscription>(SERVER_API_URL + 'api/platform/' + platform.id + '/subscription', { duration });
  }

  getToken(platform: Platform): Observable<string> {
    return this.http.get(SERVER_API_URL + 'api/platform/' + platform.id + '/token', { responseType: 'text' });
  }

  redeemGainz(platform: Platform) {
    return this.http.post<void>(SERVER_API_URL + 'api/platform/' + platform.id + '/redeem', platform.invoice);
  }

  delete(platform: Platform) {
    return this.http.delete<void>(SERVER_API_URL + 'api/platform/' + platform.id);
  }

  pushTokenToService(token: string, pushUrl: string): Observable<string> {
    return this.http.get(pushUrl + '?voucher=' + token, { responseType: 'text', withCredentials: true });
  }
}
