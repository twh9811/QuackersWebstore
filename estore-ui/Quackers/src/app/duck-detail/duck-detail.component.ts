import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';


@Component({
  selector: 'app-duck-detail',
  templateUrl: './duck-detail.component.html',
  styleUrls: ['./duck-detail.component.css']
})

export class DuckDetailComponent implements OnInit {
  private _account: Account | undefined = undefined;
  private _duckId: number | undefined = undefined;
  cart: Cart | undefined = undefined;
  duck: Duck | undefined = undefined;
  quantityInput : number = 1;

  constructor(private productService: ProductService,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private sessionService: SessionService,
    private cartService: CartService) { }

  ngOnInit(): void {
    // Checks if the user is logged in
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session?.id).subscribe(account => {
      this._account = account;
      // Validates the account's authorization (non-admin)
      this.validateAuthorization();
      
      // Gets the cart
      this.cartService.getCartAndCreate(this._account.id).then((cart) => {
        this.cart = cart;
      });

      // Gets the duckId and loads the duck if possible otherwise errors
      this.retrieveDuckId();
      if (!this._duckId) {
        this.handleInvalidDuckId(this._duckId);
        return;
      }
      this.loadDuck();
    })
  }

  /**
   * Sends the user back to the catalog page
   */
  goBack(): void {
    this.router.navigate(['/catalog']);
  }

  /**
   * Adds the duck to the cart with the specified quantity
   */
  addDuck(): void {
    this.productService.getDuck(this._duckId as number).subscribe(duck => {
      if(!duck) {
        this.notificationService.add(`Thee duck with the id of ${this._duckId} is no longer available!`, 3);
        return;
      }

      this.cartService.addItem(this.cart!, this._duckId!, this.quantityInput);
      this.cartService.updateCart(this.cart!).subscribe(response => {
        if(response.status == 200) {
          this.notificationService.add(`Successfully added ${this.quantityInput} duck(s) with the id of ${this._duckId!} to your cart!`, 3);
          return;
        }
        this.notificationService.add(`Failed to add the duck with the id of ${this._duckId!} to your cart!`, 3);
      });
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
   * Validates that a user is a customer
   * If not, they are sent back to the login page
   */
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }

  /**
   * Gets the duck id from the route and stores it in _duckId if it is an integer.
   * If the id is not an integer, they are redirected back to the catalog page
   */
  private retrieveDuckId() {
    let duckIdStr = this.route.snapshot.paramMap.get("id");
    let duckIdNum = Number.parseInt(duckIdStr!);
    if (!duckIdNum) {
      this.handleInvalidDuckId(duckIdStr);
      return;
    }

    this._duckId = duckIdNum;
  }

  /**
   * Loads the duck with the id given in the route
   * If the duckId is invalid, they are redirected back to the catalog page
   */
  private loadDuck(): void {
    this.productService.getDuck(this._duckId as number).subscribe(duck => {
      if (!duck) {
        this.handleInvalidDuckId(this._duckId);
        return;
      }

      this.duck = duck;
    });
  }

  /**
   * Sends a notification that the given id is invalid and redirects them back to the catalog page
   * @param duckIdStr The invalid id
   */
  private handleInvalidDuckId(duckIdStr: any): void {
    this.notificationService.add(`${duckIdStr} is not a valid duck id!`, 3);
    this.router.navigate(['/catalog']);
  }
}
