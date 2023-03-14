import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Duck, DuckOutfit } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';


@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html',
  styleUrls: ['./product-create.component.css']
})
export class ProductCreateComponent {
  createForm = this.formBuilder.group({
    name: '',
    quantity: 0,
    price: '',
    size: '',
    color: '',
    hatUID: 0,
    shirtUID: 0,
    shoesUID: 0,
    handItemUID: 0,
    jewelryUID: 0
  })

  constructor(private productService: ProductService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private location: Location) { }

  // TODO: MAKE ENDPOINT TO RETRIEVE COLORS AND SIZES
  /**
   * Called upon form submission
   */
  onSubmit(): void {
    // For future Reference angular validation check this.createForm.valid and remove ngNativeValidate
    if (!this.createForm.valid) {
      this.handleInvalidForm();
      return;
    }

    let formDuck = this.convertFormToDuck();
    this.productService.createDuck(formDuck).subscribe(response => {
      let status = response.status;
      switch (status) {
        case 201:
          let duck = response.body as Duck;
          this.notificationService.add(`Created a duck with an id of ${duck.id}`, 3);
          break;
        case 409:
          this.notificationService.add(`A duck already exists with the name ${formDuck.name}!`, 3)
          break;
        case 500:
          this.notificationService.add(`Something went wrong. Please try again later!`, 3)
          break;
      }
    })
  }

  /**
   * Finds the invalid controls and sends a notification that tells the user to fix the invalid controls
   */
  handleInvalidForm(): void {
    const invalid = [];
    const controls = this.createForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      if (controls[name].invalid) {
        this.notificationService.add(`${name} is invalid!`, 3)
      }
    }

  }

  /**
   * Creates a duck object from the values provided in createForm
   * 
   * @returns The created duck object
   */
  convertFormToDuck(): Duck {
    let formValue = this.createForm.value;

    let duckOutfit: DuckOutfit = {
      hatUID: formValue.hatUID as number,
      shirtUID: formValue.shirtUID as number,
      shoesUID: formValue.shoesUID as number,
      handItemUID: formValue.handItemUID as number,
      jewelryUID: formValue.jewelryUID as number,
    }

    return <Duck>{
      id: -1,
      name: formValue.name as string,
      quantity: formValue.quantity as number,
      price: formValue.price as string,
      size: formValue.size as string,
      color: formValue.color as string,
      outfit: duckOutfit
    }
  }

  goBack(): void {
    this.location.back();
  }
}
