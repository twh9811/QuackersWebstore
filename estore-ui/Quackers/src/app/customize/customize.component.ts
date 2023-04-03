import { Location } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck, DuckOutfit } from '../duck';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { Cart } from '../shopping-cart';
import { CartService } from '../shopping-cart.service';
import { SnackBarService } from '../snackbar.service';

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

  constructor(private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<CustomizeComponent>,
    @Inject(MAT_DIALOG_DATA) public account: Account) { }

  ngOnInit(): void {
   
  }

  /**
   * Called upon form submission
   */
  onSubmit(): void {
    if (!this.createForm.valid) {
      this.markAllControlsAsTouched();
      return;
    }

    let formDuck = this.convertFormToDuck();
    let observable =  this._productService.createDuck(formDuck);

    observable.subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the duck with a name of ${formDuck.name}.`);
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
          let actionStr = "create the duck";
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
      id: -1,
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