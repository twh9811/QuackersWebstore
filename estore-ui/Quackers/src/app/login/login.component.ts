import { Component } from '@angular/core';
import { Login } from '../login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';

  attempt : Login = {
    username : "",
    password : ""
  };

  onSubmit(username : String, password : String) {
      this.attempt.username = username;
      this.attempt.password = password;
  }

}
