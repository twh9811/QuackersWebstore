import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../notifications.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  notifications: string[] = [];
  shouldUpdate: boolean = false;

  constructor(public notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationService.notificationChange.subscribe(data => {
      this.notifications = data;
      this.shouldUpdate = true;
    })
  }

  update(): void {
    this.shouldUpdate = false;
  }
}
