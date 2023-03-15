import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminTestComponent } from './admin-test/admin-test.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';
import { LoginComponent } from './login/login.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { ChangePageTestComponent } from './change-page-test/change-page-test.component';

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full"},
  { path: 'adminPage', component: AdminTestComponent },
  { path: 'customerPage', component: CustomerTestComponent },
  { path: "login", component : LoginComponent},
  { path: "cart", component : ShoppingCartComponent},
  { path: "changedPage", component : ChangePageTestComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}