import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { Location } from '@angular/common';
import { AccountService } from '../account.service';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { CartService } from '../shopping-cart.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  account: Account | undefined = undefined;

  constructor(private router: Router,
    private location: Location,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService,
    private cartService: CartService) { }

  ngOnInit(): void {
  // Validates that an account is indeed logged in
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }
    
    // Gets account
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
      this.account = account;
    });
  }

  /**
  * Sends the user back to the previous page
  */
  goBack(): void {
    this.location.back();
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
