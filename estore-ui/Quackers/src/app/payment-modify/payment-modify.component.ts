import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';

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


    let observable = this.account ? this._accountService.logout(this.account) : this._accountService.createUser(this.account);

    observable.subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the account shipping information.`);
          this.close(response.body as Account);
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
   * Closes the dialog window
   * @param wasSuccessful Whether or not the duck was updated/created
   */
  close(account: Account | null): void {
    this.dialogRef.close(account);
  }

  loadAccount() {
    let controls = this.createForm.controls;

    controls.cardNumber.setValue(this.account.card);
    controls.expiration.setValue(this.account.expDate);
    controls.cvv.setValue(this.account.cvv.toString());
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
