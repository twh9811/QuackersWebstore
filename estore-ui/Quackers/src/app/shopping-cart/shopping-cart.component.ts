import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { Account } from '../account';
import { Cart } from '../shopping-cart';
import { Duck } from '../duck';
import { ProductService } from '../product.service';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  private _account: Account | undefined = undefined;
  private _cart : Cart | undefined = undefined;
  ducks: Duck[] = [];

  constructor(private router: Router,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  /**
  * Loads the ducks array when the page is opened
  */
  ngOnInit() : void {
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
      this._account = account;
      this.validateAuthorization();
      this.getCart();
      this.getDucks();
    });
  }

  getCart(): void {
    this.accountService.getCart().subscribe(_cart => this._cart = _cart);
  }

  /**
   * Gets the ducks from the product service
   */
  getDucks(): void {
    this.productService.getDucks().subscribe(ducks => this.ducks = ducks);
  }
  
  /**
  * Validates that a user is an admin
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (!this._account?.adminStatus) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }

  logout() {
    this.router.navigate([''])
  }
}
