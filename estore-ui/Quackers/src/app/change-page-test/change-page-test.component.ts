import { Component, OnInit } from '@angular/core';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-change-page-test',
  templateUrl: './change-page-test.component.html',
  styleUrls: ['./change-page-test.component.css']
})
export class ChangePageTestComponent implements OnInit {

  account! : Account;

  constructor(private accountService : AccountService, private session : SessionService) {}
  ngOnInit(): void {
    this.getAccount();
  }

  getAccount() : void {
    this.accountService.getAccount(this.session.session.id).subscribe(account => this.account = account);
  }

  

}
