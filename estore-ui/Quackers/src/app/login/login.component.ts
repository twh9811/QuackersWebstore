import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { SessionService } from '../session.service';
import { SnackbarNotificationComponent } from '../snackbar-notification/snackbar-notification.component';
import { SnackBarType } from '../snackbar-notification/snackbar-data';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent implements OnInit {
  private _account: Account | undefined;

  message = "Please login to our store to continue :)";


  loginForm = this._formBuilder.group({
    username: '',
    password: ''
  });

  constructor(private _router: Router,
    private _accountService: AccountService,
    private _session: SessionService,
    private _formBuilder: FormBuilder, private snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this._session.session = {
      type: "",
      username: "",
      plainPassword: "",
      id: -1,
      adminStatus: false
    };
  }

  /**
   * Redirects the user to the correct page (catalog for buyers, inventory for admins)
   */
  redirect(): void {
    if (!this._account) return;

    this.setSession(this._account);
    this._router.navigate([this._account.username == "admin" ? '/inventory' : '/catalog'])
    this.openSnackbar(`Welcome back ${this._account.username}!`,  SnackBarType.INFO);
  }

  /**
   * Handles login functionality
   */
  onLogin(): void {
    if (this.loginForm.invalid) return;

    const controls = this.loginForm.controls;
    this._accountService.login(controls.username.value!, controls.password.value!).subscribe(account => {
      this._account = account;

      if (!this._account) {
        this.openSnackbar("You entered an invalid password and/or username.", SnackBarType.ERROR);
        return;
      }

      this.update();
      this.redirect();
    });
  }

  /**
   * Updates the session account storage
   * @param account The account object of the user who just logged in
   */
  setSession(account: Account): void {
    this._session.session = {
      type: account.type,
      username: account.username,
      plainPassword: account.plainPassword,
      id: account.id,
      adminStatus: account.adminStatus
    };
  }

  /**
   * Handles register functionality
   */
  onRegister(): void {
    if (this.loginForm.invalid) {
      this.markAllControlsAsTouched();
      return;
    }

    const username: string = this.loginForm.controls.username.value!;
    const password: string = this.loginForm.controls.password.value!;
    // Create account with placeholders for type, id, and admin status. These DONT matter.
    // These will be reformated with proper values in AccountFileDAO based on username.
    this._account = {
      type: "UserAccount",
      id: -1,
      username: username,
      plainPassword: password,
      adminStatus: false
    };

    this._accountService.createUser(this._account).subscribe(response => {
      if (!(response instanceof HttpErrorResponse)) {
        this._account = response;
        this.update();
        this.openSnackbar("Your account was successfully created. Please login to continue.", SnackBarType.SUCCESS);
        return;
      }

      const httpResponse: HttpErrorResponse = response;
      const code: number = httpResponse.status;
      switch (code) {
        case 406:
          this.openSnackbar("Your password must be at least 8 characters long and have 1 uppercase letter, 1 lowercase letter, and 1 number.",  SnackBarType.ERROR);
          break;
        case 409:
          this.openSnackbar("An account with the name already exists. Please select a different one.", SnackBarType.ERROR);
          break;
      }

    });
  }

  /**
 * Handles password reset functionality
 */
  onPasswordReset(): void {
    if (this.loginForm.invalid) {
      this.markAllControlsAsTouched();
      return;
    }

    this.openSnackbar("This is not implemented yet.", SnackBarType.INFO);
  }

  // Updates the account variables (prevents double clicking buttons)
  update(): void {
    console.log(this._account);
  }

  /**
   * Opens a snackbar that has a close button and lasts for 3 seconds with the given message
   * @param message The message to display
   */
  private openSnackbar(message: string, type: SnackBarType): void {
    const panelClass = (type == SnackBarType.ERROR ? "snackbar-error" : (SnackBarType.SUCCESS ? "snackbar-success" : "snackbar-info"));

    this.snackbar.openFromComponent(SnackbarNotificationComponent,
      {
        data: { message: message, type: type },
        panelClass: [panelClass],
        duration: 3000
      })
    //this.snackbar.open(message, "Close", { panelClass: ["custom-style"] })
  }

  /**
   * Marks all controls as touched to allow their errors to be displayed if they aren't already
   */
  private markAllControlsAsTouched(): void {
    const controls = this.loginForm.controls;

    // Sets the type of name to the type of the attributes in <controls>
    let name: keyof typeof controls;
    for (name in controls) {
      controls[name].markAsTouched();
    }
  }

}