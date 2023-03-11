import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';

import { HttpClientModule} from '@angular/common/http';
import { InventoryManagementComponent } from './inventory-management/inventory-management.component';
import { ProductModifyComponent } from './product-modify/product-modify.component';
import { ProductCreateComponent } from './product-create/product-create.component';
import { NotificationsComponent } from './notifications/notifications.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CustomerTestComponent,
    InventoryManagementComponent,
    ProductModifyComponent,
    ProductCreateComponent,
    NotificationsComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
