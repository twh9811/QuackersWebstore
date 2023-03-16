import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  message = "Please login to our store to continue :)";
  feedback = "Fields cannot be empty, enter your information";

  account: Account | undefined;
  
  username : string = '';
  password : string = '';

  constructor(private router : Router, private accountService : AccountService, private session : SessionService) {}

  ngOnInit(): void {
    this.session.session = { 
    type : "", 
    username : "", 
    plainPassword : "", 
    id : -1, 
    adminStatus : false};
  }

  redirect() : void {
    if(this.account) {
      this.setSession(this.account);
      if(this.account.username == "admin") {
        this.router.navigate(['/adminPage']);
      } else {
        this.router.navigate(['/catalog']);
      }
    } else {
      this.feedback = "Login failed, try again or register";
    }
  }

  onLogin() : void {
    this.accountService.login(this.username, this.password).subscribe(account => {
      this.account = account; 
      this.update();
      this.redirect();
    });
  }

  setSession(account : Account) : void {
    this.session.session = {
      type : account.type, 
      username : account.username, 
      plainPassword : account.plainPassword, 
      id : account.id, 
      adminStatus : account.adminStatus};
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
        this.feedback = "Account creation failed, username already exists.";
      } else if (this.account.username == "admin") {
        this.feedback = "";
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