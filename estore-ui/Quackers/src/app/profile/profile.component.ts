import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { SnackBarService } from '../snackbar.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  account: Account | undefined = undefined;

  constructor(private _router: Router,
    private _location: Location,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService) { }

  ngOnInit(): void {
    // Validates that an account is indeed logged in
    if (!this._sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Gets account
    this._accountService.getAccount(this._sessionService.session.id).subscribe(account => {
      this.account = account;
    });
  }

  /**
  * Sends the user back to the previous page
  */
  goBack(): void {
    this._location.back();
  }

  /**
  * Validates that a user is a customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    // if this account's admin staus is true or the account is
    // undefined, then the user is sent back to the login page
    if (this.account?.adminStatus || !this.account) {
      this._snackBarService.openErrorSnackbar(`You are not authorized to view ${this._router.url}.`);
      this._router.navigate(['/']);
    }
  }
}
