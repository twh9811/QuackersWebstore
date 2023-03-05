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

  account: Account | undefined;
  
  username : string = '';
  password : string = '';

  constructor(private router : Router, private accountService : AccountService) {}

  redirect() {
    if(this.account != undefined) {
      if(this.account.username == "admin") {
      this.router.navigate(['/adminPage'])
      } else {
      this.router.navigate(['/customerPage'])
      }
    }
  }

  onSubmit() {
    this.accountService.login(this.username, this.password).subscribe(account => this.account = account);
    console.log(this.account);
    this.redirect();
  }

  

}
