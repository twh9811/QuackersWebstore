import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { MatDialog } from '@angular/material/dialog'
import { CheckoutComponent } from '../checkout/checkout.component';
@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  account: Account | undefined = undefined;
  cart: Cart | undefined = undefined;
  ducks: Duck[] = [];

  constructor(private router: Router,
    private location: Location,
    private accountService: AccountService,
    private cartService: CartService,
    private dialog: MatDialog,
    private sessionService: SessionService,
    private productService: ProductService,
    private notificationService: NotificationService,) { }

  ngOnInit(): void {
    // Validates that an account is indeed logged in
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Gets account and validates it's authorization before attempting to retrieve it's cart
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
      this.account = account;
      this.validateAuthorization();
      this.cartService.getCartAndCreate(this.account.id).then((cart) => {
        this.cart = cart;
        this.loadDucks();
      });
    });
  }

  /**
   * Gets the quantity of a given duck in a cart
   * @param duckId The duckId we are retrieving the quantity for
   * @returns The quantity if found, undefined otherwise
   */
  getDuckQuantity(duckId: number): number | undefined {
    if (!this.cart) {
      return undefined;
    }

    let result: number | undefined = this.cart.items[duckId];
    return result ? result : undefined;
  }

  /**
   * Checks if the cart has no items
   * 
   * @returns True if the cart has no items, false otherwise (including if the cart is undefined)
   */
  isCartEmpty(): boolean {
    if (!this.cart) return true;
    return Object.keys(this.cart.items).length == 0;
  }

  /**
   * Calculates the total price of a given duck in the cart (cart_quantity * duck.price)
   * 
   * @param duck The duck that the cart price is being retrieved for
   * @returns The calculated price to two decimals as a string
   */
  getTotalDuckPrice(duck: Duck): string {
    let quantity = this.getDuckQuantity(duck.id) as number;
    return (quantity * duck.price).toFixed(2);
  }

  /**
   * 
   * Calculates the total price of all of the duck in a given cart
   * 
   * @param cart 
   * @returns The total price of the cart as a string
   */
  getCartTotal(): string {
    let cartTotal: number = 0;
    //iterates over the current duck list to get the total price of the cart
    for (const duck of this.ducks) {
      const duckPrice = parseFloat(this.getTotalDuckPrice(duck));
      cartTotal += duckPrice;
    }

    return cartTotal.toFixed(2);
  }
  /**
   * Removes a given amount of a given duck from the cart
   * 
   * @param duck The duck whose quantity is being removed
   * @param quantityStr The amount to remove from the cart
   */
  removeDuck(duck: Duck, quantityStr: string): void {
    if (!this.cart) return;

    const quantity = Number.parseInt(quantityStr);
    // Shouldn't be possible but still good to check
    if (Number.isNaN(quantity)) {
      this.notificationService.add(`${quantityStr} is not a valid number!`, 3);
      return;
    }

    const newQuantity = this.getDuckQuantity(duck.id)! - quantity;
    // Shouldn't be possible but still good to check
    if (newQuantity < 0) {
      this.notificationService.add(`You can not remove more of an item than what is in your cart!`, 3);
      return;
    }

    // Remove the item from the cart if the newQuantity is 0
    if (newQuantity == 0) {
      delete this.cart.items[duck.id];
      this.ducks = this.ducks.filter(arrDuck => arrDuck.id != duck.id);
    } else {
      this.cart.items[duck.id] = newQuantity;
    }

    // Update the cart
    this.cartService.updateCart(this.cart).subscribe(status => {
      // Send success if update was a success, error otherwise
      if (status.status == 200) {
        this.notificationService.add(`Successfully removed ${quantity} duck(s) with an id of ${duck.id} from your cart!`, 3);
        return;
      }

      this.notificationService.add(`Failed to update your cart. Please try again!`, 3);
    });
  }

  /**
   * Sends the user back to the previous page
   */
  goBack(): void {
    this.location.back();
  }


  /**
   * Clears the items in the cart
   */
  clearCart(): void {
    // if this cart does not exist then return
    if (!this.cart) return;

    // sets the items and ducks to empty and then send it to the server
    this.cart.items = {};
    this.ducks = [];
    this.cartService.updateCart(this.cart).subscribe(status => {
      if (status.status == 200) {
        this.notificationService.add(`Successfully cleared your cart!`, 3);
        return;
      }

      this.notificationService.add(`Failed to update your cart. Please try again!`, 3);
    });
  }

  /**
   * Sends the user to the checkout page to purchase the items
   */
  checkoutCart(): void {
    const dialogRef = this.dialog.open(CheckoutComponent,
      {
        height: '100%',
        position: { top: '0%', right: '0%' },
        data: { account: this.account, cart: this.cart },
      });
    dialogRef.afterClosed().subscribe(() => {
      document.body.style.overflow = 'visible';
    })
    document.body.style.overflow = 'hidden';
  }

  /**
   * Retrieves the ducks that match the ids in the shopping cart and stores them in the ducks array
   * If the duck doesn't exist, it is removed from the cart
   * If the quantity of the duck in the cart exceeds the quantity available in the inventory, the quantity
   * is set to the amount available in the inventory (if that is 0, then it is removed)
   */
  private async loadDucks() {
    if (!this.cart) return;

    let shouldUpdate: boolean = false;

    // Loops through the cart items
    for (const [key, value] of Object.entries(this.cart.items)) {
      // Gets a duck for each key (a duckId)
      const duckId = Number.parseInt(key);
      // Waits for the value to be retrieved
      const duck: Duck = await firstValueFrom(this.productService.getDuck(duckId));
      // Removes duck from cart if it is no longer in the inventory
      if (!duck) {
        shouldUpdate = true;
        this.notificationService.add(`The duck with the id ${key} is no longer available!`, 3);
        delete this.cart.items[key];
        continue;
      }

      // Only add if the quantity available isn't 0
      if (duck.quantity != 0 && value != 0) this.ducks.push(duck);

      // Make sure requested quantity is available in inventory
      if (duck.quantity >= value) continue;

      // If quantity requested > quantity available, error then update cart to only have the
      // quantity available in it
      shouldUpdate = true;
      // Silences error if the value request is 0; (Item shouldn't be in cart)
      if (value != 0) {
        this.notificationService.add(`The duck with the id ${key} only has ${duck.quantity} 
          available in stock! You requested ${value}. 
          Your cart has been reflected to only have ${duck.quantity}!`, 5);
      }

      // Deletes the duck from the map if the quantity available is 0
      if (duck.quantity == 0 || value == 0) {
        delete this.cart.items[key];
        continue;
      }

      // Updates Quantity
      this.cart.items[key] = duck.quantity;
    }

    // Updates the cart if necessary
    if (shouldUpdate) {
      this.cartService.updateCart(this.cart).subscribe();
    }
  }

  /**
  * Validates that a user is a customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    // if this account's admin staus is true or the account is
    // undefined, then the user is sent back to the login page
    if (this.account?.adminStatus || !this.account) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }
}