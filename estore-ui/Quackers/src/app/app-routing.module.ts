import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerTestComponent } from './customer-test/customer-test.component';
import { LoginComponent } from './login/login.component';
import { InventoryManagementComponent } from './inventory-management/inventory-management.component';
import { ProductModifyComponent } from './product-modify/product-modify.component';
import { ProductCreateComponent } from './product-create/product-create.component';

const routes: Routes = [
  { path: "", redirectTo: "/login", pathMatch: "full"},
  { path: 'inventory', component: InventoryManagementComponent },
  { path: 'inventory/create', component: ProductCreateComponent},
  { path: 'inventory/:id', component: ProductModifyComponent},
  { path: 'customerPage', component: CustomerTestComponent },
  { path: "login", component : LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}