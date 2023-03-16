import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminTestComponent } from './admin-test/admin-test.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';
import { LoginComponent } from './login/login.component';
import { ChangePageTestComponent } from './change-page-test/change-page-test.component';
import { CatalogComponent } from './catalog/catalog.component';

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full"},
  { path: 'adminPage', component: AdminTestComponent },
  { path: 'customerPage', component: CustomerTestComponent },
  { path: "login", component : LoginComponent},
  { path: "changedPage", component : ChangePageTestComponent},
  { path: "catalog", component : CatalogComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}