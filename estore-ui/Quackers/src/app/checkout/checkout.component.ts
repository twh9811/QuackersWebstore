import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { ReceiptComponent } from '../receipt/receipt.component';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { SnackBarService } from '../snackbar.service';
import { CheckoutData } from './checkout-data';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  private _account: Account = this.checkoutData.account;
  private _cart: Cart = this.checkoutData.cart;

  detailForm = this._formBuilder.group({
    email: '',
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    zipCode: '',
    cardNumber: '',
    expiration: '',
    cvv: ''
  });

  constructor(public dialogRef: MatDialogRef<CheckoutComponent>,
    private _cartService: CartService,
    private _snackBarService: SnackBarService,
    private _formBuilder: FormBuilder,
    private _dialog: MatDialog,
    private _router: Router,
    @Inject(MAT_DIALOG_DATA) public checkoutData: CheckoutData) { }


  ngOnInit(): void {
    const controls = this.detailForm.controls;

    controls.firstName.setValue(this._account.firstName);
    controls.lastName.setValue(this._account.lastName);
    controls.address.setValue(this._account.address);
    controls.city.setValue(this._account.city);
    controls.cardNumber.setValue(this._account.card);
    controls.expiration.setValue(this._account.expDate);
    if (this._account.cvv != -1) {
      controls.cvv.setValue(this._account.cvv.toString());
    }
  }

  /**
   * Called upon form submission. Validates the form and handles checkout functionality 
   */
  onSubmit(): void {

    if (!this.detailForm.valid) {
      this.markAllControlsAsTouched();
      return;
    }

    this._cartService.validateCart(this._account.id).subscribe(response => {
      const status = response.status;
      const body: Cart = response.body;

      switch (status) {
        case 200:
          if (body == null) {
            this.handleValidCart();
          } else {
            this.handleInvalidCart(body);
          }

          break;
        case 404:
          this._snackBarService.openErrorSnackbar("Please add items to your shopping cart before attempting to checkout.");
          break;
        case 500:
          this._snackBarService.openErrorSnackbar("Uh Oh, something went wrong. Please try again.");
          break;
      }

      this.closeDialog();
    })

  }

  /**
   * Marks all controls as touched to allow their errors to be displayed if they aren't already
   */
  markAllControlsAsTouched(): void {
    const controls = this.detailForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      controls[name].markAsTouched();
    }
  }

  // Closes the checkout dialog
  closeDialog(): void {
    this.dialogRef.close();
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
    const controls = this.detailForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      if (name == controlName) return controls[name];
    }
    return null;
  }

  /**
   * Opens the receipt dialog prompt
   */
  private openReceiptPrompt(): void {
    this._dialog.open(ReceiptComponent, {
      height: 'auto',
      width: 'auto',
      data: { cart: this._cart! }
    });
  }

  /**
   * Handles cart checkout
   */
  private handleValidCart(): void {
    // TODO: Make pop that gives receipt
    this._cartService.checkoutCart(this._account.id).subscribe(response => {
      const status = response.status;
      console.log(status);
      switch (status) {
        // Theoretically shouldn't be possible due to validation before allowing checkout
        case 422:
          this._snackBarService.openErrorSnackbar("Some of the items in your cart were no longer available. We are unable to checkout your case.");
          return;
        // Theoretically shouldn't be possible due to validation before allowing checkout
        case 404:
          this._snackBarService.openErrorSnackbar("Please add items to your shopping cart before attempting to checkout.");
          return;
        case 500:
          this._snackBarService.openErrorSnackbar("Uh Oh, something went wrong. Please try again.");
          return;
      }

      // Success (for some reason status is undefined when it is 200. I do not know why)
      this._router.navigate(['catalog']);
      this.openReceiptPrompt();
    });
  }

  /**
   * Updates the invalid cart to a valid one and sends a message to the user letting them know. Also redirects the user back to the cart page
   * @param cart The validated cart
   */
  private handleInvalidCart(cart: Cart): void {
    this._cartService.updateCart(cart).subscribe(response => {
      this.closeDialog();

      // Send success if update was a success, error otherwise
      if (response.status == 200) {
        this._snackBarService.openErrorSnackbar("Some of the items in your cart were no longer available. We have adjusted your cart to remove these items.");
        return;
      }

      this._snackBarService.openErrorSnackbar("Uh Oh, we were unable to remove the invalid items from your cart.");
    });
  }

}
