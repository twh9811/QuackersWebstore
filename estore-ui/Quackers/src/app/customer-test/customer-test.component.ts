import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-test',
  templateUrl: './customer-test.component.html',
  styleUrls: ['./customer-test.component.css']
})
export class CustomerTestComponent {
  constructor(private router : Router) {}

  logout() {
    this.router.navigate([''])
  }
}
