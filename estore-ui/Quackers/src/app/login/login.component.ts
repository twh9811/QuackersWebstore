import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  message = "Please login to our store to continue :)"
  failure = "";

  account: Account | undefined;
  
  username : string = '';
  password : string = '';

  constructor(private router : Router, private accountService : AccountService) {}

  redirect() {
    if(this.account != undefined) {
      if(this.account.username == "admin") {
      this.router.navigate(['/adminPage/' + this.account.id])
      } else {
      this.router.navigate(['/customerPage/' + this.account.id])
      }
    } else {
      this.failure = "Login failed, try again"
    }
  }

  onSubmit() {
    this.accountService.login(this.username, this.password).subscribe(account => {
      this.account = account; 
      this.update();
    });
  }

  // Updates the account variable to prevent double clicking
  update() {
    console.log(this.account);
    this.redirect();
  }

}
