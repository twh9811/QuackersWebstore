import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

import { AccountService } from '../account.service';
import { Account } from '../account';
import { Session } from '../session';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-admin-test',
  templateUrl: './admin-test.component.html',
  styleUrls: ['./admin-test.component.css']
})
export class AdminTestComponent implements OnInit{
  account : Account | undefined;

  constructor(private router : Router, private accountService : AccountService, private session : SessionService) {}

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
