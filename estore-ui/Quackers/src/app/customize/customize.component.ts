import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Account } from '../account';
import { CustomDuckService } from '../custom-duck.service';
import { Duck, DuckOutfit } from '../duck';
import { SnackBarService } from '../snackbar.service';

@Component({
  selector: 'app-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.css']
})
export class CustomizeComponent implements OnInit {

  createForm = this._formBuilder.group({
    name: '',
    quantity: 1,
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
          this._snackBarService.openSuccessSnackbar(`Successfully added ${formDuck.quantity} custom duck(s) with a name of ${duckName} to your cart.`);
          this._customDuckService.sendNewDuck(this.account, response.body!)
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
   * Gets the price string or an error if a control is invalid
   * 
   * @returns The price string
   */
  getPriceString(): string {
    const controls = this.createForm.controls;

    if (!this.canPriceShow()) {
      return "In order for the price to display, you must select a size and a color and have a valid quantity inputted."
    }
    
    const individual = this.calculatePrice()
    const total = (individual * controls.quantity.value!).toFixed(2);
    let priceStr = `Price: $${total}`;

    if(controls.quantity.value! > 1) {
      priceStr += ` ($${individual.toFixed(2)} each)`
    }
    
    return priceStr;
  }

  canPriceShow(): boolean {
    const controls = this.createForm.controls;
    return this.calculatePrice() != -1 && controls.quantity.valid;
  }

  /**
   * Calculates the price of the duck
   * 
   * @returns The price of the duck
   */
  calculatePrice(): number {
    const controls = this.createForm.controls;

    if (!controls.size.valid || !controls.color.valid) {
      return -1;
    }
    let price = 0;

    // Color
    price += 1;

    switch (controls.size.value) {
      case "SMALL":
        price += 3
        break;
      case "MEDIUM":
        price += 5
        break;
      case "LARGE":
        price += 6;
        break;
    }

    if (controls.hatUID.value != "0") price += 2;
    if (controls.handItemUID.value != "0") price += 2;
    if (controls.jewelryUID.value != "0") price += 2;

    return price;
  }

  /**
   * Whether the duck preview should show
   * 
   * @returns True if the color and size are set in the form
   */
  showPreview(): boolean {
    const controls = this.createForm.controls;
    return controls.color.valid && controls.size.valid;
  }

  /**
   * Creates a duck object from the values provided in createForm
   * 
   * @returns The created duck object
   */
  convertFormToDuck(): Duck {
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
      price: 0,
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