import { Location } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck, DuckOutfit } from '../duck';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.css']
})
export class CustomizeComponent implements OnInit {

    createForm = this._formBuilder.group({
    name: '',
    quantity: 0,
    price: 0.00,
    size: '',
    color: '',
    hatUID: '0',
    handItemUID: '0',
    jewelryUID: '0'
  });
  private _account: Account | undefined = undefined;

  cart: Cart | undefined = undefined;
  ducks: Duck[] = [];
  ducksToDisplay: Duck[] = [];
  //duck: any;

  // name: String = '';
  // quantity: number = 0;
  // price: number = 0.00;
  // size_input: String = '';
  // size: String = '';
  // color: String = '';
  // color_input: String = '';
  // hatUID: number = 0;
  // handItemUID: number = 0;
  // jewelryUID: number = 0;

  constructor(private _router: Router,
    private _location: Location,
    private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _accountService: AccountService,
    private _sessionService: SessionService,
    private _cartService: CartService,
    private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<CustomizeComponent>,
    @Inject(MAT_DIALOG_DATA) public duck: Duck) { }

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
      // Waits for the cart to load before loading the ducks
      // This prevents issues such as the cart not loading
      this._cartService.getCartAndCreate(this._account.id).then(cart => {
        if (!cart) {
          this._router.navigate(['/']);
          this._snackBarService.openErrorSnackbar("Unable to load your cart!");
          return;
        }

        this.cart = cart;
        //this.getDucks();
      })
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

  /**
  * Validates that a user is an customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this._snackBarService.openErrorSnackbar(`You are not authorized to view ${this._router.url}.`);
      this._router.navigate(['/']);
    }
  }

    /**
   * Add a duck to shopping cart
   * 
   * @param duck The duck being added
   * @param quantity The number of ducks to add
   */
  addDuck(duck: Duck, quantityStr: string): void {

    // Makes sure the number is in the form of x or x.00 (there can be as many 0s as they'd like)
    if (!quantityStr.match(/^\d+(\.[0]+)?$/g)) {
      this._snackBarService.openErrorSnackbar(`You must enter an integer value for the quantity input.`);
      return;
    }

    const quantity = Number.parseInt(quantityStr);

    this._cartService.addItem(this.cart!, duck.id, quantity);
    this._cartService.updateCart(this.cart!).subscribe(response => {
      if (response.status == 200) {
        this._snackBarService.openSuccessSnackbar(`Successfully added ${quantity} duck(s) with the name of ${duck.name} to your cart.`);
        return;
      }
      this._snackBarService.openErrorSnackbar(`Failed to add the duck with the name of ${duck.name} to your cart.`);
    });
  }

  /**
   * Sends the user back to the previous page
   */
  goBack(): void {
    this._location.back();
  }
  

  // makeDuck(name: String, size: String, color: String, hatID: number, handID: number, jewelryID: number): Duck{
    
  //   let duckOutfit: DuckOutfit = {
  //     hatUID: hatID,
  //     shirtUID: 0,
  //     shoesUID: 0,
  //     handItemUID: handID,
  //     jewelryUID: jewelryID,
  //   };

  //   return <Duck>{
  //     id: this.duck ? this.duck.id : -1,
  //     name: name,
  //     quantity: 1,
  //     price: 10,
  //     size: size,
  //     color: color,
  //     outfit: duckOutfit
  //   };
  // }

  onSubmit(): void {
    if (!this.createForm.valid) {
      this.markAllControlsAsTouched();
      return;
    }

    let formDuck = this.convertFormToDuck();
    let observable = this.duck ? this._productService.updateDuck(formDuck) : this._productService.createDuck(formDuck);

    observable.subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the duck with a name of ${this.duck.name}.`);
          this.close(response.body as Duck);
          break;
        // Duck Update Reponse - Not possible in theory 
        case 404:
          this._snackBarService.openErrorSnackbar(`Unable to find the requested duck.`);
          this.close(null);
          break;
        // Duck Creation Response
        case 201:
          let duck = response.body as Duck;
          this._snackBarService.openSuccessSnackbar(`Successfully created a new duck with a name of ${duck.name}.`);
          this.close(duck);
          break;
        // Both Duck Update and Creation Response
        case 409:
          this._snackBarService.openErrorSnackbar(`A duck already exists with the name ${formDuck.name}.`);
          break;
        // Both Duck Creation and Update Response - Shouldn't be possible due to form validation
        case 400:
          let actionStr = this.duck ? "update the given duck" : "create the duck";
          this._snackBarService.openErrorSnackbar(`Failed to ${actionStr} due to a bad value being entered.`);
          break;
        // Both Duck Creation and Update Response
        case 500:
          this._snackBarService.openErrorSnackbar(`Something went wrong. Please try again later.`);
          break;
      }
    });
  }

  /**
   * Closes the dialog window
   * @param wasSuccessful Whether or not the duck was updated/created
   */
  close(duck: Duck | null): void {
    this.dialogRef.close(duck);
  }
  /**
   * Creates a duck object from the values provided in createForm
   * 
   * @returns The created duck object
   */
  private convertFormToDuck(): Duck {
    let formValue = this.createForm.value;

    let duckOutfit: DuckOutfit = {
      hatUID: Number.parseInt(formValue.hatUID!),
      shirtUID: 0,
      shoesUID: 0,
      handItemUID: Number.parseInt(formValue.handItemUID!),
      jewelryUID: Number.parseInt(formValue.jewelryUID!),
    };

    return <Duck>{
      id: this.duck ? this.duck.id : -1,
      name: formValue.name as string,
      quantity: formValue.quantity as number,
      price: formValue.price as number,
      size: formValue.size as string,
      color: formValue.color as string,
      outfit: duckOutfit
    };
  }
    /**
   * Marks all controls as touched to allow their errors to be displayed if they aren't already
   */
    markAllControlsAsTouched(): void {
      const controls = this.createForm.controls;
  
      // Sets the type of name to the type of the attributes in <controls>
      let name: keyof typeof controls;
      for (name in controls) {
        controls[name].markAsTouched();
      }
    }
}