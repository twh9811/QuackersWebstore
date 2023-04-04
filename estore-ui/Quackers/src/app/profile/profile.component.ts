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
    private _dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public account: Account) { }

  ngOnInit(): void {
    this.dialogRef.backdropClick().subscribe(() => {
      this.dialogRef.close(this.account);
    })
  }

  /**
   * Gets an accounts cvv if there is one
   * @returns The cvv or an empty string
   */
  getCVV(): string {
    return this.account.cvv == -1 ? '' : this.account.cvv.toString();
  }

  /**
   * Opens change shipping menu
   */
  changeShippingAddress(): void {
    const dialogRef = this._dialog.open(ShippingModifyComponent, { data: this.account });

    dialogRef.afterClosed().subscribe((account) => {
      if (account != null) {
        this.account = account;
      }
      document.body.style.overflowY = 'visible';
    });

    document.body.style.overflowY = 'none';
  }

  /**
   * Opens change payment menu
   */
  changePaymentMethod(): void {
    const dialogRef = this._dialog.open(PaymentModifyComponent, { data: this.account });

    dialogRef.afterClosed().subscribe((account) => {
      if (account != null) {
        this.account = account;
      }
      document.body.style.overflowY = 'visible';
    });

    document.body.style.overflowY = 'none';
  }

}
