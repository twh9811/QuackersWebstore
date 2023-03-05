import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  message = "Please login to our store to continue :)"

  account: Account = {
    id: null,
    username: "",
    password: "",
    adminStatus: null
  };

  username : String = '';
  password : String = '';

  constructor(private router : Router) {}

  redirect() {
    if(this.username == "admin") {
      this.router.navigate(['/adminPage'])
    } else {
      this.router.navigate(['/customerPage'])
    }
  }

  onSubmit(username : String, password : String) {
      this.username = username;
      this.password = password;
      this.redirect();
  }

  

}
