import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { NotificationService } from '../notification.service';
import { ProductService } from '../product.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  private _account: Account | undefined = undefined;

  detailForm = this.formBuilder.group({
    email: '',
    firstName: '',
    lastName: '',
    address: '',
    apartment: '',
    city: '',
    zipCode: 0,
    cardNumber: '',
    cvv: '',
    expiration: ''
  });

  constructor(private productService: ProductService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  ngOnInit(): void {
    // DELETE THIS
    this.sessionService.session = {
      type: "UserAccount",
      id: 1,
      username: "w",
      plainPassword: "",
      adminStatus: false
    }

    if (!this.sessionService.session) {
      this.validateAuthorization();
      return;
    }

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session?.id).subscribe(account => {
      this._account = account;

      this.validateAuthorization();
    })
  }

  onSubmit(): void {
    if (!this.detailForm.valid) {
      this.handleInvalidForm();
      return;
    }
    // Check validation of cart and form
    // If invalid send back to cart and provide error
  }

  private handleInvalidForm(): void {
    const controls = this.detailForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      let control = controls[name];
      
      if (!control.invalid) {
        continue;
      }

      switch (name) {
        case "cardNumber":
          this.notificationService.add(`${name} must be in the form XXXX XXXX XXXX XXXX!`, 3);
          continue;
        case "expiration":
          this.notificationService.add(`${name} must be in the form of MM/YYYY!`, 3);
          continue;
        case "cvv":
          this.notificationService.add(`${name} must be in the form of XXX!`, 3);
          continue;
      }

      if (typeof control.value === "number") {
        this.notificationService.add(`${name} must be greater than or equal to 0!`, 3);
        continue;
      }

      this.notificationService.add(`${name} is a required field!`, 3);
    }
  }

  /**
  * Validates that a user is an customer
  * If not, they are sent back to the login page
  */
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }
}
