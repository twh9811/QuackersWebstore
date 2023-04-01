import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck } from '../duck';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { SnackBarService } from '../snackbar.service';

@Component({
  selector: 'app-inventory-management',
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.css']
})
export class InventoryManagementComponent implements OnInit {
  private _account: Account | undefined = undefined;
  ducks: Duck[] = [];


  constructor(private _router: Router,
    private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService) { }

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
      this.getDucks();
    });
  }

  getDuckColorImage(duck: Duck): string {
    if(duck.size == "EXTRA_LARGR") return "";

    const color = duck.color.toLowerCase();
    const colorFile = color.charAt(0).toUpperCase() + color.slice(1);
    console.log(`/assets/duck-colors/${duck.size}/${colorFile}.png`)
    return `/assets/duck-colors/${duck.size}/${colorFile}.png`;
  }

  /**
   * Gets the ducks from the product service
   */
  getDucks(): void {
    this._productService.getDucks().subscribe(ducks => this.ducks = ducks);
  }

  /**
   * Sends the user to the product modification screen for a given duck
   * 
   * @param id The id of the duck
   */
  goToDuckModification(id: number): void {
    this._router.navigate([`/inventory/product/${id}`]);
  }

  /**
  * Validates that a user is an admin
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (!this._account?.adminStatus) {
      this._snackBarService.openErrorSnackbar(`You are not authorized to view ${this._router.url}!`)
      this._router.navigate(['/']);
    }
  }

  /**
   * Deletes a given duck
   * 
   * @param duck The duck being deleted
   */
  deleteDuck(duck: Duck): void {
    this.ducks = this.ducks.filter(a_duck => a_duck != duck);
    this._productService.deleteDuck(duck.id).subscribe(httpResponse => {
      switch (httpResponse.status) {
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully deleted the duck with the id ${duck.id}.`);
          break;
        case 404:
          this._snackBarService.openErrorSnackbar(`Failed to delete the duck with the id ${duck.id} because it does not exist!`);
          break;
        default:
          this._snackBarService.openErrorSnackbar(`Failed to delete the duck with the id ${duck.id} because something went wrong.`);
          console.error(httpResponse.statusText);
      }
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
}
