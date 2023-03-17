import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private notifications: string[] = [];
  private _notificationChange: Subject<string[]> = new Subject();

  constructor() { }

  /**
   * Gets the _notificationChange object
   * 
   * @return The _notificationChange object
   */
  public get notificationChange(): Subject<string[]> {
    return this._notificationChange;
  }
  /**
   * Adds a notification to the notifications array
   * 
   * @param notification The notification being added to the notifications array
   * @param expireIn The amount of time in seconds to auto remove the notification. 0 or less will not auto delete
   * @returns The index of the newly added notification
   */
  add(notification: string, expireIn: number = -1): void {
    this.notifications.push(notification);
    this.notificationChange.next(this.notifications);

    if (expireIn <= 0) {
      return;
    }

    setTimeout(() => {
      this.deleteNotification(notification);
    }, expireIn * 1000);
  }

  /**
   * Deletes a specific notification
   * 
   * @param notifiction The notification being deleted
   */
  deleteNotification(notifiction: String): void {
    this.notifications = this.notifications.filter((a_notif) => a_notif != notifiction);
    this.notificationChange.next(this.notifications);
  }

  /**
   * Clears the notifications
   */
  clear(): void {
    this.notifications = [];
    this.notificationChange.next(this.notifications);
  }

}
