import { Injectable } from '@angular/core';
import { Account } from './account';
import { Session } from './session';

@Injectable({
  providedIn: 'root'
})
export class SessionService implements Session{
  private _session! : Account;

  constructor() { }

  public set session(account : Account ) {
    this._session = account;
  }

  public get session() : Account{
    return this._session;
  }

}
