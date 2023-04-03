import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
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
      card: '',
      expDate: '',
      cvv: 0
    });

  constructor(private _accountService: AccountService,
    private _snackBarService: SnackBarService,
    private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ShippingModifyComponent>,
    @Inject(MAT_DIALOG_DATA) public account: Account) { }

  ngOnInit(): void {
    if(this.account != null) {
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
   * Closes the dialog window
   * @param wasSuccessful Whether or not the duck was updated/created
   */
  close(account: Account | null): void {
    this.dialogRef.close(account);
  }

  loadAccount() {
    let controls = this.createForm.controls;
    controls.firstName.setValue(this.account.firstName);
    controls.lastName.setValue(this.account.lastName);
    controls.address.setValue(this.account.address);
    controls.city.setValue(this.account.city);
    controls.zipCode.setValue(this.account.zipCode);

    controls.city.setValue(this.account.card);
    controls.expDate.setValue(this.account.expDate);
    controls.cvv.setValue(this.account.cvv);
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
