import { Component } from '@angular/core';
import { Login } from '../login';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  message = "Please login to our store to continue :)"
  username = '';
  password = '';

  attempt : Login = {
    username : "",
    password : ""
  };

  constructor(private router : Router) {}

  onSubmit(username : String, password : String) {
      this.attempt.username = username;
      this.attempt.password = password;
      this.redirect(this.attempt);
  }

  redirect(account : Login) {
    if(account.username == "admin") {
      this.router.navigate(['/adminPage'])
    } else {
      this.router.navigate(['/customerPage'])
    }
  }

}
