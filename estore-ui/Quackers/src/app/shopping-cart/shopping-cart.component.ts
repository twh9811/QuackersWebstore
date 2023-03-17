import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';

import { Account } from '../account';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent {

  account : Account | undefined;

  constructor(private router : Router, private route : ActivatedRoute, private accountService : AccountService, private session : SessionService ) {}

  ngOnInit() : void {
    this.getAccount();
  }

  getAccount() : void {
    this.accountService.getAccount(this.session.session.id).subscribe(account => this.account = account);
  }
  logout() {
    this.router.navigate([''])
  }
}
