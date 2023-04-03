import { Injectable } from '@angular/core';
import { SnackBarType } from './snackbar-notification/snackbar-data';
import { SnackbarNotificationComponent } from './snackbar-notification/snackbar-notification.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private _snackbar: MatSnackBar) { }


  /**
   * Opens a success snackbar
   * 
   * @param message The message to display
   * @param duration The amount of time in seconds to keep the snackbar open for
   */
  public openSuccessSnackbar(message: string, duration: number = 3): void {
    this.openSnackbar(message, SnackBarType.SUCCESS, duration * 1000);
  }

  /**
   * Opens an error snackbar
   * 
   * @param message The message to display
   * @param duration The amount of time in seconds to keep the snackbar open for
   */
  public openErrorSnackbar(message: string, duration: number = 3): void {
    this.openSnackbar(message, SnackBarType.ERROR, duration * 1000);
  }

  /**
   * Opens an info snackbar
   * 
   * @param message The message to display
   * @param duration The amount of time in seconds to keep the snackbar open for
   */
  public openInfoSnackbar(message: string, duration: number = 3): void {
    this.openSnackbar(message, SnackBarType.INFO, duration * 1000);
  }

  /**
   * Opens a snackbar that has a close button and lasts for 3 seconds with the given message
   * 
   * @param message The message to display
   * @param type The type of snackbar to open (success, error, info)
   * @param duration The amount of time in ms to keep the snackbar open for
   */
  private openSnackbar(message: string, type: SnackBarType, duration: number = 3): void {
    const panelClass = (type == SnackBarType.ERROR ? "snackbar-error" : (type == SnackBarType.SUCCESS ? "snackbar-success" : "snackbar-info"));

    this._snackbar.openFromComponent(SnackbarNotificationComponent,
      {
        data: { message: message, type: type },
        panelClass: [panelClass],
        duration: duration
      });
  }
}
