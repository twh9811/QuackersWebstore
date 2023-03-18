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
      this.loadDuck();
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

  goBack(): void {
    this.router.navigate(['/catalog']);
  }

  /**
   * Validates that a user is a customer
   * If not, they are sent back to the login page
   */
  private validateAuthorization(): void {
    if (this._account?.adminStatus) {
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


}
