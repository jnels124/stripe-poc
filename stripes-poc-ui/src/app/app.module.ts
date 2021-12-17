import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CustomerComponent } from './customer/customer.component';
import {FormsModule} from "@angular/forms";
import {StripeService} from "./service/stripe.service";
import {HttpClientModule} from "@angular/common/http";
import { CustomerDetailComponent } from './customer-detail/customer-detail.component';
import { ChargeComponent } from './charge/charge.component';
import { ConnectedAccountComponent } from './connected-account/connected-account.component';
import { ChargeDetailsComponent } from './charge-details/charge-details.component';

@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    CustomerDetailComponent,
    ChargeComponent,
    ConnectedAccountComponent,
    ChargeDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
