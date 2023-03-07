import { Component } from '@angular/core';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent {

  notifications: string[] = [];
  shouldUpdate: boolean = false;

  constructor(public notificationService: NotificationService) { }

  ngOnInit() {
    this.notificationService.notificationChange.subscribe(data => {
      this.notifications = data;
      this.shouldUpdate = true;
    })
  }

  update() {
    this.shouldUpdate = false;
  }
}
