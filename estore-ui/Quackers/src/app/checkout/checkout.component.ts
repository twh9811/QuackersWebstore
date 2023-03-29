import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { NotificationService } from '../notification.service';
import { ReceiptComponent } from '../receipt/receipt.component';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { CheckoutData } from './checkout-data';
import { ErrorStateMatcher } from '@angular/material/core';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  private _account: Account = this.checkoutData.account;
  private _cart: Cart = this.checkoutData.cart;

  detailForm = this.formBuilder.group({
    email: '',
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    zipCode: '',
    cardNumber: '',
    cvv: '',
    expiration: ''
  });

  constructor(public dialogRef: MatDialogRef<CheckoutComponent>,
    private cartService: CartService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public checkoutData: CheckoutData) { }


  //TODO: Fix error message overlap, change expiration to date selector


  /**
   * Called upon form submission. Validates the form and handles checkout functionality 
   */
  onSubmit(): void {

    if (!this.detailForm.valid) {
      this.markAllControlsAsTouched();
      return;
    }

    this.cartService.validateCart(this._account.id).subscribe(response => {
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
          this.notificationService.add("Please add items to your shopping cart before attempting to checkout.", 3)
          break;
        case 500:
          this.notificationService.add("Uh Oh, something went wrong. Please try again.", 3);
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
    this.dialog.open(ReceiptComponent, {
      height: 'auto',
      width: 'auto',
      data: { cart: this._cart! }
    })
  }

  /**
   * Handles cart checkout
   */
  private handleValidCart(): void {
    // TODO: Make pop that gives receipt
    this.cartService.checkoutCart(this._account.id).subscribe(response => {
      const status = response.status;
      console.log(status);
      switch (status) {
        // Theoretically shouldn't be possible due to validation before allowing checkout
        case 422:
          this.notificationService.add("Some of the items in your cart were no longer available. We are unable to checkout your case.", 3);
          return;
        // Theoretically shouldn't be possible due to validation before allowing checkout
        case 404:
          this.notificationService.add("Please add items to your shopping cart before attempting to checkout.", 3);
          return;
        case 500:
          this.notificationService.add("Uh Oh, something went wrong. Please try again.", 3);
          return;
      }

      // Success (for some reason status is undefined when it is 200. I do not know why)
      this.router.navigate(['catalog']);
      this.openReceiptPrompt();
    });
  }

  /**
   * Updates the invalid cart to a valid one and sends a message to the user letting them know. Also redirects the user back to the cart page
   * @param cart The validated cart
   */
  private handleInvalidCart(cart: Cart): void {
    this.cartService.updateCart(cart).subscribe(response => {
      this.closeDialog();

      // Send success if update was a success, error otherwise
      if (response.status == 200) {
        this.notificationService.add("Some of the items in your cart were no longer available. We have adjusted your cart to remove these items.", 3);
        return;
      }

      this.notificationService.add("Uh Oh, we were unable to remove the invalid items from your cart.", 3);
    });
  }

}
