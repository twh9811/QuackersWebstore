import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

import { AccountService } from '../account.service';
import { Account } from '../account';

@Component({
  selector: 'app-admin-test',
  templateUrl: './admin-test.component.html',
  styleUrls: ['./admin-test.component.css']
})
export class AdminTestComponent implements OnInit{
  account : Account | undefined;

  constructor(private router : Router, private route : ActivatedRoute, private accountService : AccountService, private location : Location) {}

  ngOnInit() : void {
    this.getAccount();
  }

  getAccount() : void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!);
    this.accountService.getAccount(id).subscribe(account => this.account = account);
  }

  logout() {
    this.router.navigate([''])
  }
}
