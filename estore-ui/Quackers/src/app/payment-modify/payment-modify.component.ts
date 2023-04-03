import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-payment-modify',
  templateUrl: './payment-modify.component.html',
  styleUrls: ['./payment-modify.component.css']
})
export class PaymentModifyComponent {
  createForm = this._formBuilder.group({
    cardNumber: '',
    expiration: '',
    cvv: ''
  });

  constructor(private _accountService: AccountService,
    private _snackBarService: SnackBarService,
    private _sessionService: SessionService,
    private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<PaymentModifyComponent>,
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
          this._snackBarService.openSuccessSnackbar(`Successfully updated the account payment information.`);
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
     * Whether an error should be displayed or not.
     * Will display in the following circumstances:
     *  1. The control is not allowed to be empty but is
     *  2. The control is not empty but does not meet some other validator (i.e. a pattern)
     * 
     * @param controlName The name of the control
     * @param ignoreRequired Whether or not to ignore the required validator
     * @returns True if the error should be display
     */
  shouldDisplayError(controlName: string, ignoreRequired: boolean): boolean {
    const control = this.getControl(controlName);
    if (!control) return true;

    // If the control is not invalid, the error should not be displayed
    if (!control.invalid) return false;

    // If the user has not touched the control, the error should not be displayed
    if (!control.touched) return false;

    // If the control is not allowed to be empty, the error should be displayed
    if (!ignoreRequired) return true;

    const value = control.value;
    // The control is allowed to be empty
    // If the control's value is undefined or has a length of 0, the error should not displayed
    if (!value || value.length == 0) return false;

    return true;
  }

  /**
   * Gets a form control by name
   * 
   * @param controlName The name of the control
   * @returns The control if found, otherwise null
   */
  getControl(controlName: string): FormControl<string | null> | null {
    const controls = this.createForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      if (name == controlName) return controls[name];
    }
    return null;
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
      firstName: this.account.firstName,
      lastName: this.account.lastName,
      address: this.account.address,
      city: this.account.city,
      zipCode: this.account.zipCode,
      card: controls.cardNumber.value,
      expDate: controls.expiration.value,
      cvv: Number.parseInt(controls.cvv.value!)
    }
  }

  /**
   * Loads the accounts values into the form
   */
  loadAccount() {
    let controls = this.createForm.controls;

    controls.cardNumber.setValue(this.account.card);
    controls.expiration.setValue(this.account.expDate);
    if (this.account.cvv != -1) {
      controls.cvv.setValue(this.account.cvv.toString());
    }
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
