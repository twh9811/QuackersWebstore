import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Quackers Duck Emporium';

  // router needs to be public for angular to compile (used in app.component.html)
  constructor(public router: Router) {}

  /**
  * If called, the site will logout the user and
  * redirect the user back to the login page
  * 
  */
  logout() : void {
    this.router.navigate([''])
  }
}
