import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { Router } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminTestComponent } from './admin-test/admin-test.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminTestComponent,
    CustomerTestComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
