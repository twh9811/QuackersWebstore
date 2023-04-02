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

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {
  private _account: Account | undefined = undefined;

  cart: Cart | undefined = undefined;
  ducks: Duck[] = [];
  ducksToDisplay: Duck[] = [];

  constructor(private _router: Router,
    private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService,
    private _cartService: CartService) { }

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
        this.getDucks();
      })
    });
  }

  /**
   * Gets the path to the base image for a duck 
   * 
   * @param duck The duck
   * @returns The path to the image
   */
  getDuckColorImage(duck: Duck): string {
    if (duck.size == "EXTRA_LARGE") return "";

    const color = duck.color.toLowerCase();
    const colorFile = color.charAt(0).toUpperCase() + color.slice(1);
    return `/assets/duck-colors/${duck.size}/${colorFile}.png`;
  }

  /**
   * Gets the path to a given accessory's image
   * 
   * @param accessoryName The name of the accessory
   * @param duck The duck 
   * @returns The path to the image
   */
  getAccessoryImage(accessoryName: string, duck: Duck): string {
    const outfit: any = duck.outfit;
    if (outfit[accessoryName + "UID"] == 0) return "";

    return `/assets/duck-${accessoryName}/${outfit[accessoryName + "UID"]}.png`;
  }

  /**
   * Gets the css class for a given duck accessory
   * 
   * @param accessoryName The name of the accessory
   * @param duck The duck
   * @returns The name of the css class for the accessory
   */
  getCSSClass(accessoryName: string, duck: Duck): string {
    const outfit: any = duck.outfit;
    return `duck-${accessoryName}-${outfit[accessoryName + "UID"]}-${duck.size.toLowerCase()}`;
  }

  /**
   * Updates the ducks being displayed on screen
   * 
   * @param ducks The new array of ducks
   */
  updateDisplayDucks(ducks: Observable<Duck[]>) {
    ducks.subscribe(duckArr => this.ducksToDisplay = duckArr)
  }

  /**
   * Gets the ducks from the product service
   */
  getDucks(): void {
    this._productService.getDucks().subscribe(ducks => {
      this.ducks = ducks.filter(duck => duck.quantity != 0)
      this.ducksToDisplay = this.ducks;
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
      this._snackBarService.openErrorSnackbar(`You are not authorized to view ${this._router.url}!`);
      this._router.navigate(['/']);
    }
  }

  /**
   * Add a duck to shopping cart
   * 
   * @param duckId The id of the duck being added
   */
  addDuck(duckId: number): void {
    this._productService.getDuck(duckId as number).subscribe(duck => {

      if (!duck || duck.quantity < 1) {
        this._snackBarService.openErrorSnackbar(`The duck with the id of ${duckId} is no longer available!`);
        return;
      }

      this._cartService.addItem(this.cart!, duckId!, 1);
      this._cartService.updateCart(this.cart!).subscribe(response => {
        if (response.status == 200) {
          this._snackBarService.openSuccessSnackbar(`Successfully added one duck with the id of ${duckId} to your cart!`);
          return;
        }
        this._snackBarService.openErrorSnackbar(`Failed to add the duck with the id of ${duckId} to your cart!`);
      });
    });
  }

  /**
   * Show details for a duck
   * 
   * @param duck The duck being showed
   */
  showDuck(id: number): void {
    this._router.navigate([`/catalog/${id}`]);
  }
}
