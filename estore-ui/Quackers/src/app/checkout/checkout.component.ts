import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { NotificationService } from '../notification.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { MatDialog } from '@angular/material/dialog';
import { ReceiptComponent } from '../receipt/receipt.component';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  private _account: Account | undefined = undefined;
  private _cart: Cart | undefined = undefined;

  detailForm = this.formBuilder.group({
    email: '',
    firstName: '',
    lastName: '',
    address: '',
    apartment: '',
    city: '',
    zipCode: 0,
    cardNumber: '',
    cvv: '',
    expiration: ''
  });

  constructor(private cartService: CartService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private router: Router,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  ngOnInit(): void {
    // DELETE THIS
    this.sessionService.session = {
      type: "UserAccount",
      id: 1,
      username: "w",
      plainPassword: "",
      adminStatus: false
    }

    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session?.id).subscribe(account => {
      this._account = account;

      this.validateAuthorization();
      this.cartService.getCartAndCreate(this._account.id).then((cart) => {
        this._cart = cart;
      });
    })
  }

  onSubmit(): void {
    if (!this._account) {
      return;
    }

    if (!this.detailForm.valid) {
      this.handleInvalidForm();
      return;
    }

    this.cartService.validateCart(this._account.id).subscribe(response => {
      const status = response.status;
      const body: Cart = response.body;

      switch (status) {
        case 200:
          if (body == null) {
            this.handleValidCart();
            return;
          }

          this.handleInvalidCart(body);
          break;
        case 404:
          this.notificationService.add("Please add items to your shopping cart before attempting to checkout.", 3)
          break;
        case 500:
          this.notificationService.add("Uh Oh, something went wrong. Please try again.", 3);
          break;
      }
    })

  }

  private openReceiptPrompt(): void {
    const dialogRef = this.dialog.open(ReceiptComponent, {
      width: '250px',
      data: { cart: this._cart! }
    })
  }

  /**
   * Handles cart checkout
   */
  private handleValidCart(): void {
    if (!this._account) {
      return;
    }

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
      this.router.navigate(['cart']);

      // Send success if update was a success, error otherwise
      if (response.status == 200) {
        this.notificationService.add("Some of the items in your cart were no longer available. We have adjusted your cart to remove these items.", 3);
        return;
      }

      this.notificationService.add("Uh Oh, we were unable to remove the invalid items from your cart.", 3);
    });
  }

  /**
   * Sends an error message detailing what form controls are invalid
   */
  private handleInvalidForm(): void {
    const controls = this.detailForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      let control = controls[name];

      if (!control.invalid) {
        continue;
      }

      switch (name) {
        case "cardNumber":
          this.notificationService.add(`${name} must be in the form XXXX XXXX XXXX XXXX.`, 3);
          continue;
        case "expiration":
          this.notificationService.add(`${name} must be in the form of MM/YYYY.`, 3);
          continue;
        case "cvv":
          this.notificationService.add(`${name} must be in the form of XXX.`, 3);
          continue;
      }

      if (typeof control.value === "number") {
        this.notificationService.add(`${name} must be greater than or equal to 0.`, 3);
        continue;
      }

      this.notificationService.add(`${name} is a required field.`, 3);
    }
  }

  /**
  * Validates that a user is an customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }
}
