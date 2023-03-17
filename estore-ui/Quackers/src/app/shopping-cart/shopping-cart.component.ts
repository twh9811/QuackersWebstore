import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
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

  account : Account | undefined;
  cart : Cart | undefined;
  ducks: Duck[] = [];

  constructor(private router: Router,
    private route : ActivatedRoute,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  /**
  * Loads the ducks array when the page is opened
  */
  ngOnInit() : void {
    // Waits for account to be retrieved before doing anything else
    this.getAccount();
    this.validateAuthorization();
    this.getCart();
    this.getDucks();
  }

  getAccount() : void {
    this.accountService.getAccount(this.session.session.id).subscribe(account => this.account = account);
  }

  getCart(): void {
    this.accountService.getCart().subscribe(cart => this.cart = cart);
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
    if (!this.account?.adminStatus) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }

  logout() {
    this.router.navigate([''])
  }
}

@Component({
  selector: 'app-customer-test',
  templateUrl: './customer-test.component.html',
  styleUrls: ['./customer-test.component.css']
})
export class ShoppingCartComponent implements OnInit {
  account : Account | undefined;
  cart : Cart | undefined;
  ducks: Duck[] = [];

  constructor(private router : Router,
    private route : ActivatedRoute,
    private accountService : AccountService,
    private session : SessionService,
    private productService: ProductService,
    private notificationService: NotificationService, ) {}

  ngOnInit() : void {
    this.getAccount();
    this.getCart();
    this.getDucks();
  }

  getAccount() : void {
    this.accountService.getAccount(this.session.session.id).subscribe(account => this.account = account);
  }

  getCart(): void {
    this.accountService.getCart(this.session.session.id).subscribe(cart => this.cart = cart);
  }

  /**
   * Gets the ducks from the product service
   */
  getDucks(): void {
    this.productService.getDucks().subscribe(ducks => this.ducks = ducks);
  }
}
