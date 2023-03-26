import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';



@Component({
  selector: 'navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent {

  _account: Account | undefined = undefined;
  adminStatus : boolean = false;


  constructor(public router: Router,
    private accountService: AccountService,
    private sessionService: SessionService) { }

  /**
  * Loads the ducks array when the page is opened
  */
  ngOnInit(): void {

    // Waits for account to be retrieved before doing anything else
    this.accountService.getAccount(this.sessionService.session.id).subscribe(account => {
      this._account = account;
      this.adminStatus = this.checkAdminStatus()

    });
  }

  /**
  * Validates that a user is an customer
  * If not, they are sent back to the login page
  
  private validateAuthorization(): void {
    if (this._account?.adminStatus || !this._account) {
      this.notificationService.add(`You are not authorized to view ${this.router.url}!`, 3);
      this.router.navigate(['/']);
    }
  }
  */

  checkAdminStatus() : boolean {
    if (this._account?.adminStatus || !this._account){
      return true;
    }
    return false;
  }

  logout() : void {
    this.router.navigate([''])
  }
}