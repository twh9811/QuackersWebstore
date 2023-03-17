import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Duck, DuckOutfit } from '../duck';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-duck-detail',
  templateUrl: './duck-detail.component.html',
  styleUrls: ['./duck-detail.component.css']
})

export class DuckDetailComponent implements OnInit {
  private _account: Account | undefined = undefined;
  private _duckId: number | undefined = undefined;
  createForm = this.formBuilder.group({
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

  constructor(private productService: ProductService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  ngOnInit(): void {
    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session?.id).subscribe(account => {
      this._account = account;

      this.validateAuthorization();
      this.retrieveDuckId();
      if (this._duckId) {
        this.loadDuck();
      }
    })
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
    let observable = this._duckId ? this.productService.updateDuck(formDuck) : this.productService.createDuck(formDuck);

    observable.subscribe(response => {
      let status = response.status;
      switch (status) {
        // Duck Update Response
        case 200:
          this.notificationService.add(`Successfully updated the duck with an id of ${this._duckId}`, 3);
          break;
        // Duck Update Reponse - Not possible in theory 
        case 404:
          this.notificationService.add(`Unable to find a duck with an id of ${this._duckId}`, 3);
          break;
        // Duck Creation Response
        case 201:
          let duck = response.body as Duck;
          this.notificationService.add(`Created a duck with an id of ${duck.id}`, 3);
          break;
        // Both Duck Update and Creation Response
        case 409:
          this.notificationService.add(`A duck already exists with the name ${formDuck.name}!`, 3);
          break;
        // Both Duck Creation and Update Response - Shouldn't be possible due to form validation
        case 400:
          let actionStr = this._duckId ? "update the given duck" : "create the duck";
          this.notificationService.add(`Failed to ${actionStr} due to a bad value being entered!`, 3);
          break;
        // Both Duck Creation and Update Response
        case 500:
          this.notificationService.add(`Something went wrong. Please try again later!`, 3);
          break;
      }
    });

    this.goBack();
  }

  goBack(): void {
    this.router.navigate(['/inventory']);
  }

  /**
   * Validates that a user is an admin
   * If not, they are sent back to the login page
   */
  private validateAuthorization(): void {
    if (!this._account?.adminStatus) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }

  /**
   * Gets the duck id from the route and stores it in _duckId if it is an integer.
   * If the id is not an integer, they are redirected back to the inventory page
   */
  private retrieveDuckId() {
    let duckIdStr = this.route.snapshot.paramMap.get("id");

    // No id was provided. Therefore, we are creating a new duck.
    if (!duckIdStr) {
      this._duckId = undefined;
      return;
    }

    let duckIdNum = Number.parseInt(duckIdStr!);
    if (!duckIdNum) {
      this.handleInvalidDuckId(duckIdStr);
      return;
    }

    this._duckId = duckIdNum;
  }

  /**
   * Loads the duck with the id given in the route
   * If the duckId is invalid, they are redirected back to the inventory page
   */
  private loadDuck(): void {
    this.productService.getDuck(this._duckId as number).subscribe(duck => {
      if (!duck) {
        this.handleInvalidDuckId(this._duckId);
        return;
      }

      let duckOutfit: DuckOutfit = duck.outfit;
      let controls = this.createForm.controls;

      controls.name.setValue(duck.name);
      controls.quantity.setValue(duck.quantity);
      controls.price.setValue(duck.price);
      controls.size.setValue(duck.size);
      controls.color.setValue(duck.color);

      controls.hatUID.setValue(duckOutfit.hatUID);
      controls.shirtUID.setValue(duckOutfit.shirtUID);
      controls.shoesUID.setValue(duckOutfit.shoesUID);
      controls.handItemUID.setValue(duckOutfit.handItemUID);
      controls.jewelryUID.setValue(duckOutfit.jewelryUID);
    });
  }

  /**
   * Sends a notification that the given id is invalid and redirects them back to the inventory page
   * @param duckIdStr The invalid id
   */
  private handleInvalidDuckId(duckIdStr: any): void {
    this.notificationService.add(`${duckIdStr} is not a valid duck id!`, 3);
    this.router.navigate(['/inventory']);
  }


  /**
   * Finds the invalid controls and sends a notification that tells the user to fix the invalid controls
   */
  private handleInvalidForm(): void {
    const invalid = [];
    const controls = this.createForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      let control = controls[name];
      if (!control.invalid) {
        continue;
      }

      if (typeof control.value === "number") {
        this.notificationService.add(`${name} must be greater than or equal to 0!`, 3);
        continue;
      }

      if (name === "name") {
        this.notificationService.add(`${name} must not be empty or blank!`, 3);
        continue;
      }

      this.notificationService.add(`${name} is a required field!`, 3);
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
