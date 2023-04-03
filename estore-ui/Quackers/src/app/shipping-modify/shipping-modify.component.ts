import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
import { SessionService } from '../session.service';
@Component({
  selector: 'app-shipping-modify',
  templateUrl: './shipping-modify.component.html',
  styleUrls: ['./shipping-modify.component.css']
})
export class ShippingModifyComponent implements OnInit {

  createForm = this._formBuilder.group({
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    zipCode: '',
  });

  constructor(private _accountService: AccountService,
    private _sessionService: SessionService,
    private _snackBarService: SnackBarService,
    private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ShippingModifyComponent>,
    @Inject(MAT_DIALOG_DATA) public account: Account) { }

  ngOnInit(): void {
    if (this.account != null) {
      this.loadAccount();
    }
  }


  /**
   * Called upon form submission
   */
  onSubmit(): void {
    if (!this.createForm.valid) {
      this.markAllControlsAsTouched();
      return;
    }

    this._accountService.updateAccount(this.updateAccount()).subscribe(response => {
      let status = response.status;
      switch (status) {
        // Success
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the account shipping information.`);
          this._sessionService.session = response.body!;
          this.dialogRef.close(response.body);
          break;
        // Account not found - should be impossible
        case 400:
          this._snackBarService.openErrorSnackbar(`Unable to find your account. Please logout and log back in before trying again.`);
          this.dialogRef.close();
          break;
        // Password too weak - should be impossible
        case 422:
          this._snackBarService.openErrorSnackbar(`You must make your password stronger before you can change these settings.`);
          this.dialogRef.close();
          break;
        // Server error
        case 500:
          this._snackBarService.openErrorSnackbar(`Uh Oh! Something went wrong. Please try again later.`);
          this.dialogRef.close();
          break;
      }
    });

  }

  /**
   * Converts the form to an account
   * 
   * @returns A new account object with the updated values
   */
  updateAccount(): Account {
    const controls = this.createForm.controls;

    return <Account>{
      type: this.account.type,
      id: this.account.id,
      username: this.account.username,
      plainPassword: this.account.plainPassword,
      adminStatus: this.account.adminStatus,
      firstName: controls.firstName.value!,
      lastName: controls.lastName.value!,
      address: controls.address.value!,
      city: controls.city.value,
      zipCode: controls.zipCode.value,
      card: this.account.card,
      expDate: this.account.expDate,
      cvv: this.account.cvv
    }
  }

  /**
   * Loads the accounts values into the form
   */
  loadAccount(): void {
    const controls = this.createForm.controls;
    controls.firstName.setValue(this.account.firstName);
    controls.lastName.setValue(this.account.lastName);
    controls.address.setValue(this.account.address);
    controls.city.setValue(this.account.city);
    controls.zipCode.setValue(this.account.zipCode);
  }

  /**
   * Marks all controls as touched to allow their errors to be displayed if they aren't already
  */
  markAllControlsAsTouched(): void {
    const controls = this.createForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      controls[name].markAsTouched();
    }
  }

}
