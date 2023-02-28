import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  message = "Please login to our store to continue :)"

  username : String = '';
  password : String = '';
  isLoggedIn : Boolean = false;

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
      this.isLoggedIn = true;
      this.redirect();
  }

  

}
