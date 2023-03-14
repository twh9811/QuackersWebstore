import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerTestComponent } from './customer-test/customer-test.component';
import { InventoryManagementComponent } from './inventory-management/inventory-management.component';
import { LoginComponent } from './login/login.component';
import { ProductCreateComponent } from './product-create-modify/product-create-modify.component';

const routes: Routes = [
  { path: "", redirectTo: "/login", pathMatch: "full"},
  { path: 'inventory', component: InventoryManagementComponent },
  { path: 'inventory/product', component: ProductCreateComponent},
  { path: 'inventory/product/:id', component: ProductCreateComponent},
  { path: 'customerPage', component: CustomerTestComponent },
  { path: "login", component : LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}