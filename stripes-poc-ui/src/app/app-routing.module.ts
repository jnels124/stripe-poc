import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomerComponent} from "./customer/customer.component";
import {CustomerDetailComponent} from "./customer-detail/customer-detail.component";
import {ChargeComponent} from "./charge/charge.component";
import {ConnectedAccountComponent} from "./connected-account/connected-account.component";
import {ChargeDetailsComponent} from "./charge-details/charge-details.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'customer',
    pathMatch: 'full'
  },
  {
    path: 'customer',
    component: CustomerComponent
  },
  {
    path: 'customer/:id',
    component: CustomerDetailComponent
  },
  {
    path: 'charge',
    component: ChargeComponent
  },
  {
    path: 'charge/:id',
    component: ChargeDetailsComponent
  },
  {
    path: 'connectedAccount',
    component: ConnectedAccountComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
