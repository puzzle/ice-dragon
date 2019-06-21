import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class IcedragonService {
  constructor(private http: HttpClient) {}

  getChallenge(pubKey: string): Observable<string> {
    return this.http.post(SERVER_API_URL + 'api/recklessuser/challenge', { nodePublicKey: pubKey }, { responseType: 'text' });
  }

  loginReckless(pubKey: string, signature: string): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/recklessuser/login', { nodePublicKey: pubKey, challengeResponse: signature });
  }
}
