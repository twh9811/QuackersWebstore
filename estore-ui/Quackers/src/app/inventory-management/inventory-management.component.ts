import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-inventory-management',
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.css']
})
export class InventoryManagementComponent implements OnInit {
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
   * Sends the user to the product modification screen for a given duck
   * 
   * @param id The id of the duck
   */
  goToDuckModification(id: number): void {
    this.router.navigate([`/inventory/product/${id}`]);
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

  /**
   * Deletes a given duck
   * 
   * @param duck The duck being deleted
   */
  deleteDuck(duck: Duck): void {
    this.ducks = this.ducks.filter(a_duck => a_duck != duck);
    this.productService.deleteDuck(duck.id).subscribe(httpResponse => {
      switch (httpResponse.status) {
        case 200:
          this.notificationService.add(`Successfully deleted the duck with the id ${duck.id}.`, 3);
          break;
        case 404:
          this.notificationService.add(`Failed to delete the duck with the id ${duck.id} because it does not exist!`, 3);
          break;
        default:
          this.notificationService.add(`Failed to delete the duck with the id ${duck.id} because something went wrong.`, 3);
          console.error(httpResponse.statusText);
      }
    })
  }
}
