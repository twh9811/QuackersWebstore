import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Session } from '../session';
import { SessionService } from '../session.service';

import { AccountService } from '../account.service';
import { Account } from '../account';

@Component({
  selector: 'app-customer-test',
  templateUrl: './customer-test.component.html',
  styleUrls: ['./customer-test.component.css']
})
export class CustomerTestComponent {
  account : Account | undefined;

  constructor(private router : Router, private route : ActivatedRoute, private accountService : AccountService, private session : SessionService ) {}

  ngOnInit() : void {
    this.getAccount();
  }

  getAccount() : void {
    this.accountService.getAccount(this.session.session.id).subscribe(account => this.account = account);
  }

  logout() : void {
    this.router.navigate([''])
  }
}
