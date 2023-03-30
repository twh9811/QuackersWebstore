import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {
  private _account: Account | undefined = undefined;

  cart: Cart | undefined = undefined;
  ducks: Duck[] = [];

  constructor(private router: Router,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService,
    private cartService: CartService) { }

  /**
  * Loads the ducks array when the page is opened
  */
  ngOnInit(): void {
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
      this._account = account;
      this.validateAuthorization();
      // Waits for the cart to load before loading the ducks
      // This prevents issues such as the cart not loading
      this.cartService.getCartAndCreate(this._account.id).then(cart => {
        if (!cart) {
          this.router.navigate(['/']);
          this.notificationService.add("Unable to load your cart!", 3);
          return;
        }

        this.cart = cart;
        this.getDucks();
      })
    });
  }

  /**
   * Gets the ducks from the product service
   */
  getDucks(): void {
    this.productService.getDucks().subscribe(ducks => this.ducks = ducks.filter(duck => duck.quantity != 0));
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

  /**
   * Add a duck to shopping cart
   * 
   * @param duckId The id of the duck being added
   */
  addDuck(duckId: number): void {
    this.productService.getDuck(duckId as number).subscribe(duck => {

      if (!duck || duck.quantity < 1) {
        this.notificationService.add(`Thee duck with the id of ${duckId} is no longer available!`, 3);
        return;
      }

      this.cartService.addItem(this.cart!, duckId!, 1);
      this.cartService.updateCart(this.cart!).subscribe(response => {
        if (response.status == 200) {
          this.notificationService.add(`Successfully added one duck with the id of ${duckId} to your cart!`, 3);
          return;
        }
        this.notificationService.add(`Failed to add the duck with the id of ${duckId} to your cart!`, 3);
      });
    });
  }

  /**
   * Show details for a duck
   * 
   * @param duck The duck being showed
   */
  showDuck(id: number): void {
    this.router.navigate([`/catalog/${id}`]);
  }
}
