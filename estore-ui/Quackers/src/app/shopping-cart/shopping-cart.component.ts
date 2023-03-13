import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent {
  constructor(private router : Router) {}

  logout() {
    this.router.navigate([''])
  }
}
