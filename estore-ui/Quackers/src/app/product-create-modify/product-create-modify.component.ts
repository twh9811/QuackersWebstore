import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Duck, DuckOutfit } from '../duck';
import { ProductService } from '../product.service';
import { SnackBarService } from '../snackbar.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-product-create-modify',
  templateUrl: './product-create-modify.component.html',
  styleUrls: ['./product-create-modify.component.css']
})
export class ProductCreateComponent implements OnInit {

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
    public dialogRef: MatDialogRef<ProductCreateComponent>,
    @Inject(MAT_DIALOG_DATA) public duck: Duck) { }

  ngOnInit(): void {
    if(this.duck != null) {
      this.loadDuck();
    }
  }

  /**
   * Called upon form submission
   */
  onSubmit(): void {
    if (!this.createForm.valid) {
      this.handleInvalidForm();
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
          this._snackBarService.openSuccessSnackbar(`Created a duck with an id of ${duck.id}.`);
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
   * Loads the duck with the id given in the route
   * If the duckId is invalid, they are redirected back to the inventory page
   */
  private loadDuck(): void {
      let duckOutfit: DuckOutfit = this.duck.outfit;
      let controls = this.createForm.controls;

      controls.name.setValue(this.duck.name);
      controls.quantity.setValue(this.duck.quantity);
      controls.price.setValue(this.duck.price);
      controls.size.setValue(this.duck.size);
      controls.color.setValue(this.duck.color);

      controls.hatUID.setValue(duckOutfit.hatUID.toString());
      controls.handItemUID.setValue(duckOutfit.handItemUID.toString());
      controls.jewelryUID.setValue(duckOutfit.jewelryUID.toString());
  }

  /**
   * Finds the invalid controls and sends a notification that tells the user to fix the invalid controls
   */
  private handleInvalidForm(): void {
    const controls = this.createForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      let control = controls[name];
      if (!control.invalid) {
        continue;
      }

      if (typeof control.value === "number") {
        this._snackBarService.openErrorSnackbar(`${name} must be greater than or equal to 0!`);
        continue;
      }

      if (name === "name") {
        this._snackBarService.openErrorSnackbar(`${name} must not be empty or blank!`);
        continue;
      }

      this._snackBarService.openErrorSnackbar(`${name} is a required field!`);
    }

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
}
