import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Duck } from '../duck';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-inventory-management',
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.css']
})
export class InventoryManagementComponent {
  ducks: Duck[] = [];

  constructor(private router: Router, private productService: ProductService) { }

  /**
   * Loads the ducks array when the page is opened
   */
  ngOnInit() {
    this.getDucks();
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
  goToDuckModification(id: number) {
    this.router.navigate([`/inventory/${id}`]);
  }

  /**
   * Sends the user to the duck creation screen
   */
  goToDuckCreation() {
    this.router.navigate(['/inventory/create']);
  }

  /**
   * Deletes a given duck
   * 
   * @param duck The duck being deleted
   */
  deleteDuck(duck: Duck) {
    this.ducks = this.ducks.filter(a_duck => a_duck != duck);
    this.productService.deleteDuck(duck.id);
  }

  /**
   * Logs out the user (this will be moved eventually)
   */
  logout() {
    this.router.navigate(['']);
  }
}
