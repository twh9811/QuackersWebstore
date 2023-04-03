import { Location } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ShippingModifyComponent } from '../shipping-modify/shipping-modify.component';
import { PaymentModifyComponent } from '../payment-modify/payment-modify.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {


  constructor(public dialogRef: MatDialogRef<ProfileComponent>,
    private _router: Router,
    private _location: Location,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService,
    private _dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public account: Account) { }

  ngOnInit(): void {

  }

  /**
  * Sends the user back to the previous page
  */
  goBack(): void {
    this._location.back();
  }

  changeShippingAddress(): void {
    const dialogRef = this._dialog.open(ShippingModifyComponent, { data: this.account });
    dialogRef.afterClosed().subscribe((obj) => {

    })

  }

  changePaymentMethod(): void {
    const dialogRef = this._dialog.open(PaymentModifyComponent, { data: this.account });
    dialogRef.afterClosed().subscribe((obj) => {
      document.body.style.overflowY = 'visible';
    })
  }

}
