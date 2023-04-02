import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck, DuckOutfit } from '../duck';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';
import { SnackBarService } from '../snackbar.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CheckoutData } from '../checkout/checkout-data';

@Component({
  selector: 'app-product-create-modify',
  templateUrl: './product-create-modify.component.html',
  styleUrls: ['./product-create-modify.component.css']
})
export class ProductCreateComponent implements OnInit {
  private _account: Account | undefined = undefined;
  private _duckId: number | undefined = undefined;

  createForm = this._formBuilder.group({
    name: '',
    quantity: 0,
    price: 0.00,
    size: '',
    color: '',
    hatUID: 0,
    shirtUID: 0,
    shoesUID: 0,
    handItemUID: 0,
    jewelryUID: 0
  });

  constructor(private _productService: ProductService,
    private _snackBarService: SnackBarService,
    private _formBuilder: FormBuilder,
    private _router: Router,
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
    let observable = this._duckId ? this._productService.updateDuck(formDuck) : this._productService.createDuck(formDuck);

    observable.subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 200:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the duck with an id of ${this._duckId}.`);
          break;
        // Duck Update Reponse - Not possible in theory 
        case 404:
          this._snackBarService.openErrorSnackbar(`Unable to find a duck with an id of ${this._duckId}.`);
          break;
        // Duck Creation Response
        case 201:
          let duck = response.body as Duck;
          this._snackBarService.openSuccessSnackbar(`Created a duck with an id of ${duck.id}.`);
          break;
        // Both Duck Update and Creation Response
        case 409:
          this._snackBarService.openErrorSnackbar(`A duck already exists with the name ${formDuck.name}.`);
          break;
        // Both Duck Creation and Update Response - Shouldn't be possible due to form validation
        case 400:
          let actionStr = this._duckId ? "update the given duck" : "create the duck";
          this._snackBarService.openErrorSnackbar(`Failed to ${actionStr} due to a bad value being entered.`);
          break;
        // Both Duck Creation and Update Response
        case 500:
          this._snackBarService.openErrorSnackbar(`Something went wrong. Please try again later.`);
          break;
      }
    });

    this.goBack();
  }

  goBack(): void {
    this._router.navigate(['/inventory']);
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

      controls.hatUID.setValue(duckOutfit.hatUID);
      controls.shirtUID.setValue(duckOutfit.shirtUID);
      controls.shoesUID.setValue(duckOutfit.shoesUID);
      controls.handItemUID.setValue(duckOutfit.handItemUID);
      controls.jewelryUID.setValue(duckOutfit.jewelryUID);
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
      hatUID: formValue.hatUID as number,
      shirtUID: formValue.shirtUID as number,
      shoesUID: formValue.shoesUID as number,
      handItemUID: formValue.handItemUID as number,
      jewelryUID: formValue.jewelryUID as number,
    };

    return <Duck>{
      id: this._duckId ? this._duckId : -1,
      name: formValue.name as string,
      quantity: formValue.quantity as number,
      price: formValue.price as number,
      size: formValue.size as string,
      color: formValue.color as string,
      outfit: duckOutfit
    };
  }
}
