import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminTestComponent } from './admin-test/admin-test.component';
import { CustomerTestComponent } from './customer-test/customer-test.component';

const routes: Routes = [
  { path: 'adminPage', component: AdminTestComponent },
  { path: 'customerPage', component: CustomerTestComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }