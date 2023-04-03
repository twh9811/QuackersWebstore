import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Duck } from '../duck';
import { ProductService } from '../product.service';
import { ReceiptData } from './receipt-data';


@Component({
  selector: 'app-receipt',
  templateUrl: './receipt.component.html',
  styleUrls: ['./receipt.component.css']
})

export class ReceiptComponent implements OnInit {
  ducks: Duck[] = [];
  cart = this.receiptData.cart;
  customDucks = this.receiptData.customDucks;

  constructor(private _productService: ProductService,
    @Inject(MAT_DIALOG_DATA) public receiptData: ReceiptData
  ) { }

  ngOnInit() {
    this.loadDucks();
  }

  /**
   * Gets the quantity of a given duck in a cart
   * @param duckId The duckId we are retrieving the quantity for
   * @returns The quantity if found, undefined otherwise
   */
  getDuckQuantity(duckId: number): number | undefined {
    if (!this.cart) {
      return undefined;
    }

    let result: number | undefined = this.cart.items[duckId];
    return result ? result : undefined;
  }

  /**
   * Calculates the total price of a given duck in the cart (cart_quantity * duck.price)
   * 
   * @param duck The duck that the cart price is being retrieved for
   * @returns The calculated price to two decimals as a string
   */
  getTotalDuckPrice(duck: Duck): string {
    let quantity = this.getDuckQuantity(duck.id) as number;
    return (quantity * duck.price).toFixed(2);
  }

  /**
   * Calculates the total price of a given custom duck in the cart (duck.quantity * duck.price)
   * 
   * @param duck The custom duck that the cart price is being retrieved for
   * @returns The calculated price to two decimals as a string
   */
  getTotalCustomDuckPrice(duck: Duck): string {
    return (duck.quantity * duck.price).toFixed(2);
  }

  /**
   * 
   * Calculates the total price of all of the duck in a given cart
   * 
   * @param cart 
   * @returns The total price of the cart as a string
   */
  getCartTotal(): string {
    let cartTotal: number = 0;

    // Gets premade duck total
    for (const duck of this.ducks) {
      const duckPrice = parseFloat(this.getTotalDuckPrice(duck));
      cartTotal += duckPrice;
    }

    // Gets custom duck total
    for (const duck of this.customDucks) {
      const duckPrice = parseFloat(this.getTotalCustomDuckPrice(duck));
      cartTotal += duckPrice;
    }

    return cartTotal.toFixed(2);
  }

  private loadDucks(): void {
    for (const [duckIdStr, quantity] of Object.entries(this.cart.items)) {
      const duckId = Number.parseInt(duckIdStr);
      this._productService.getDuck(duckId).subscribe(duck => this.ducks.push(duck));
    }
  }

}
