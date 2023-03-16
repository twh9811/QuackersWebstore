import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private notifications: string[] = [];
  notificationChange: Subject<string[]> = new Subject();

  constructor() { }

  /**
   * Adds a notification to the notifications array
   * 
   * @param notification The notification being added to the notifications array
   * @param expireIn The amount of time in seconds to auto remove the notification. -1 will not auto delete
   * @returns The index of the newly added notification
   */
  add(notification: string, expireIn: number = -1): number {
    let index = this.notifications.push(notification) - 1
    this.notificationChange.next(this.notifications);

    if (expireIn > 0) {
      setTimeout(() => {
        this.deleteNotification(notification);
      }, expireIn * 1000);
    }

    return index;
  }

  /**
   * Deletes a notification at a specific index
   * 
   * @param index The index of the notification
   */
  deleteAt(index: number): void {
    this.notifications = this.notifications.filter((_, notificationIndex) => notificationIndex != index);
    this.notificationChange.next(this.notifications);
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