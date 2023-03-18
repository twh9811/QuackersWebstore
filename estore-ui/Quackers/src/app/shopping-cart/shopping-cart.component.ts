import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { Account } from '../account';
import { Cart } from '../shopping-cart';
import { Duck } from '../duck';
import { CartService } from '../shopping-cart.service';
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

  constructor(private router : Router,
    private route : ActivatedRoute,
    private accountService : AccountService,
    private cartService : CartService,
    private sessionService : SessionService,
    private productService: ProductService,
    private notificationService: NotificationService, ) {}

  ngOnInit() : void {

    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    this.getAccount();
    if( this.getCart() == undefined ){
      this.createCart();
    }
    this.getCart();   
    //turn the hashmap into somehting readable
    this.viewCart(); 
  }

  getAccount() : void {
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => this.account = account);
  }

  getCart(): void {
    this.cartService.getCart(this.sessionService.session.id).subscribe(cart => this.cart = cart);
  }

  createCart() : void {
    this.cart = {
     customerId : this.sessionService.session.id,
     items : new Map<string, number>()
   };
    this.cartService.createCart(this.cart).subscribe(cart => this.cart = cart);
  }


  viewCart() : void {

  }

  clearCart() : void {
    if(this.getCart){
      this.cart.items.clear();
    this.cartService.updateCart(this.cart).subscribe(cart => this.cart = cart);
    }
  }

  /**
  * Validates that a user is a customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (this.account?.adminStatus) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }



}
