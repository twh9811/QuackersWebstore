import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Quacker\'s Duck Emporium';

  // router needs to be public for angular to compile (used in app.component.html)
  constructor(public router: Router) {}

  goToAccount() : void {
    this.router.navigate(['/account'])
  }
  logout() : void {
    this.router.navigate([''])
  }
}
