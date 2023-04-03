import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { MatDialog } from '@angular/material/dialog';
import { ProfileComponent } from '../profile/profile.component';

@Component({
  selector: 'navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  _account: Account | undefined = undefined;
  adminStatus: boolean = false;

  // router needs to be public for angular to compile (used in app.component.html)
  constructor(public router: Router,
    private _accountService: AccountService,
    private _sessionService: SessionService,
    private _dialog: MatDialog,
  ) { }

  /**
  * api will first check if an account currently exist and the admin status of the account
  */
  ngOnInit(): void {
    const navObservable = this.router.events.subscribe(event => {
      if (!(event instanceof NavigationEnd)) return;

      this.ngOnInit();
      navObservable.unsubscribe();
    })
    

    // Waits for account to be retrieved before doing anything else
    this._accountService.getAccount(this._sessionService.session.id).subscribe(account => {
      this._account = account;
      this.adminStatus = this.checkAdminStatus();

    });
  }

  openProfileDialog(): void {
    this._dialog.open(ProfileComponent, {
      height: '100%',
      position: { top: '0%', right: '0%' },
      data: this._account
    })
  }

  /**
  * checks the admin status of the user
  * 
  * @returns True if the user is an admin, false otherwise (including if the cart is undefined)
  */
  checkAdminStatus(): boolean {
    if (this._account?.adminStatus || !this._account) {
      return true;
    }
    return false;
  }

  /**
  * If called, the site will logout the user and
  * redirect the user back to the login page
  * 
  */
  logout(): void {
    this.router.navigate(['']);
  }
}