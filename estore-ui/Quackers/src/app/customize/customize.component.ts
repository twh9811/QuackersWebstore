import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.css']
})
export class CustomizeComponent implements OnInit {

    createForm = this._formBuilder.group({
    name: '',
    quantity: 0,
    price: 0.00,
    size: '',
    color: '',
    hatUID: '0',
    handItemUID: '0',
    jewelryUID: '0'
  });
  private _account: Account | undefined = undefined;

  cart: Cart | undefined = undefined;
  ducks: Duck[] = [];
  ducksToDisplay: Duck[] = [];

  constructor(private _router: Router,
    private _location: Location,
    private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService,
    private _cartService: CartService,
    private _formBuilder: FormBuilder,) { }

  /**
   * Loads the ducks array when the page is opened
   */
  ngOnInit(): void {
    if (!this._sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this._accountService.getAccount(this._sessionService.session.id).subscribe(account => {
      this._account = account;
      this.validateAuthorization();
      // Waits for the cart to load before loading the ducks
      // This prevents issues such as the cart not loading
      this._cartService.getCartAndCreate(this._account.id).then(cart => {
        if (!cart) {
          this._router.navigate(['/']);
          this._snackBarService.openErrorSnackbar("Unable to load your cart!");
          return;
        }

        this.cart = cart;
        //this.getDucks();
      })
    });
}
  /**
   * Gets the price of a duck in the form of $x.xx
   * 
   * @param duck The duck that the cart price is being retrieved for
   * @returns The calculated price to two decimals as a string
   */
  getDuckPrice(duck: Duck): string {
    return `$${(duck.price).toFixed(2)}`;
  }

  /**
  * Validates that a user is an customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this._snackBarService.openErrorSnackbar(`You are not authorized to view ${this._router.url}.`);
      this._router.navigate(['/']);
    }
  }

    /**
   * Add a duck to shopping cart
   * 
   * @param duck The duck being added
   * @param quantity The number of ducks to add
   */
  addDuck(duck: Duck, quantityStr: string): void {

    // Makes sure the number is in the form of x or x.00 (there can be as many 0s as they'd like)
    if (!quantityStr.match(/^\d+(\.[0]+)?$/g)) {
      this._snackBarService.openErrorSnackbar(`You must enter an integer value for the quantity input.`);
      return;
    }

    const quantity = Number.parseInt(quantityStr);

    this._cartService.addItem(this.cart!, duck.id, quantity);
    this._cartService.updateCart(this.cart!).subscribe(response => {
      if (response.status == 200) {
        this._snackBarService.openSuccessSnackbar(`Successfully added ${quantity} duck(s) with the name of ${duck.name} to your cart.`);
        return;
      }
      this._snackBarService.openErrorSnackbar(`Failed to add the duck with the name of ${duck.name} to your cart.`);
    });
  }

  /**
   * Sends the user back to the previous page
   */
  goBack(): void {
    this._location.back();
  }
  

}