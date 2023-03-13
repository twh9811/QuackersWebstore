import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  message = "Please login to our store to continue :)"
  feedback = "";

  account: Account | undefined;
  
  username : string = '';
  password : string = '';

  constructor(private router : Router, private accountService : AccountService) {}

  redirect() : void {
    if(this.account) {
      if(this.account.username == "admin") {
        this.router.navigate(['/adminPage/' + this.account.id])
      } else {
        this.router.navigate(['/customerPage/' + this.account.id])
      }
    } else {
      this.feedback = "Login failed, try again or register"
    }
  }

  onLogin() : void {
    this.accountService.login(this.username, this.password).subscribe(account => {
      this.account = account; 
      this.update();
      this.redirect();
    });
  }

  onRegister(username : string, password : string) : void {
    // Create account with placeholders for type, id, and admin status. These DONT matter.
    // These will be reformated with proper values in AccountFileDAO based on username.
    this.account = {
      type: "UserAccount",
      id : -1,
      username : username,
      plainPassword : password,
      adminStatus : false
    };

    this.accountService.createUser(this.account).subscribe(account => {
      this.account = account;
      this.update();
      // Account creation failed since account is now undefined. This means username already exists since it relies on null being returned from the DAO
      if(this.account == undefined) {
        this.feedback = "Account creation failed, username already exists."
      } else if (this.account.username == "admin") {
        this.feedback = ""
      } else {
        this.feedback = "Account created, please login.";
      }
    });
  }

  // Updates the account variables (prevents double clicking buttons)
  update() : void {
    console.log(this.account);
  }

}