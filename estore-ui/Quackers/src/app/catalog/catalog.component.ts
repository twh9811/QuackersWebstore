import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit{
  private _account: Account | undefined = undefined;
  ducks: Duck[] = [];

  constructor(private router: Router,
    private productService: ProductService,
    private notificationService: NotificationService,
    private accountService: AccountService,
    private sessionService: SessionService) { }

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
        this.getDucks();
      });
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
    if(this._account?.adminStatus) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }
   /**
   * Add a duck to shopping cart
   * 
   * @param duck The duck being added
   */
  addDuck(duck: Duck): void {
    
  }
}
