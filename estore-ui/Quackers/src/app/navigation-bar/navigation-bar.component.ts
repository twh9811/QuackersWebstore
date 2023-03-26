import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';


@Component({
  selector: 'navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent {

  constructor(public router: Router,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService,
    private cartService: CartService) { }

    private _account: Account | undefined = undefined;
    account: Account | undefined;
    isAdmin: boolean | undefined;

    ngOnInit(): void {

      //if the user is logged in then the site will Wait for account to be retrieved before doing anything else
      if (this.router.url != '/login') {
        this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
          this._account = account;
        });
      }
    }

    checkAdminStatus() : boolean {
      if (this._account?.adminStatus){
        return true;
      }
      return false;
    }

    logout() : void {
      this.router.navigate([''])
    }
}