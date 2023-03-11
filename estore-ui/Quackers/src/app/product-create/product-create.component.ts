import { HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Duck } from '../duck';
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
    quantity: '',
    price: '',
    size: '',
    color: '',
  })

  constructor(private productService: ProductService, private notificationService: NotificationService, private formBuilder: FormBuilder) { }

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

    // Cast to any so I can insert the id property
    let formValues = this.createForm.value as any;
    // Id doesn't matter so it's set to -1
    formValues.id = -1;
    // <formValues> now has the same fields as the Duck interface, so <formValues> can be passed to <productService#createDuck>
    this.productService.createDuck(formValues as Duck).subscribe(response => {
      let status = response.status;
      switch (status) {
        case 201:
          let duck = response.body as Duck;
          this.notificationService.add(`Created a duck with an id of ${duck.id}`, 3);
          break;
        case 409:
          this.notificationService.add(`A duck already exists with the name ${formValues.name}!`, 3)
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
    for(name in controls) {
      if(controls[name].invalid) {
        this.notificationService.add(`${name} is invalid!`, 3)
      }
    }
   
  }
}
