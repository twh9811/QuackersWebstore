import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import { HttpClientModule} from '@angular/common/http';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { InventoryManagementComponent } from './inventory-management/inventory-management.component';
import { ProductCreateComponent } from './product-create-modify/product-create-modify.component';
import { CatalogComponent } from './catalog/catalog.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { DucksearchComponent } from './ducksearch/ducksearch.component';
import { DuckDetailComponent } from './duck-detail/duck-detail.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { ProfileComponent } from './profile/profile.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ShoppingCartComponent,
    InventoryManagementComponent,
    ProductCreateComponent,
    NotificationsComponent,
    CatalogComponent,
    DucksearchComponent,
    DuckDetailComponent,
    ProfileComponent,
    NavigationBarComponent
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
