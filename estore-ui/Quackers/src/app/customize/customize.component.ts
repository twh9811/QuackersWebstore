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
import { CustomDuckService } from '../custom-duck.service';

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

  constructor(private _customDuckService: CustomDuckService,
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
    // Needed because createDuckForAccount modifies the duck's name
    const duckName = formDuck.name;

    this._customDuckService.createDuckForAccount(this.account, formDuck).subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 201:
          this._snackBarService.openSuccessSnackbar(`Successfully updated the duck with a name of ${duckName}.`);
          this.dialogRef.close();
          break;
        // Duck Update Reponse - Not possible in theory 
        case 409:
          this._snackBarService.openErrorSnackbar(`You already have a custom duck with the name ${duckName}.`);
          break;
        // Both Duck Creation and Update Response
        case 500:
          this._snackBarService.openErrorSnackbar(`Something went wrong. Please try again later.`);
          break;
      }
    });

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