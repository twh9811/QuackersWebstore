import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { Router } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminTestComponent } from './admin-test/admin-test.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';

import { HttpClientModule} from '@angular/common/http';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component'

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminTestComponent,
    CustomerTestComponent,
    ShoppingCartComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }