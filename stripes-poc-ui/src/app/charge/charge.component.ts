import { Component, OnInit } from '@angular/core';
import {Charge} from "../model/charge";
import {StripeService} from "../service/stripe.service";
import {PaymentMethod} from "../model/payment-method";
import {ConnectedAccount} from "../model/connected-account";
import {Router} from "@angular/router";

@Component({
  selector: 'app-charge',
  templateUrl: './charge.component.html',
  styleUrls: ['./charge.component.scss']
})
export class ChargeComponent implements OnInit {
  chargeInfo: Charge = new Charge;
  customerPaymentMethodMap: {[key: string]: Array<PaymentMethod>} = {};
  connectedAccounts: Array<ConnectedAccount> = [];

  constructor(private stripeService: StripeService, private router: Router) { }

  ngOnInit(): void {
    this.stripeService.listCustomers().subscribe(response => {
      response.forEach(customer => {
        // should join observables TODO ... or just grab payment methods on selection
        this.stripeService.getPaymentMethods(customer.id)
          .subscribe(paymentMethods => this.customerPaymentMethodMap[customer.id] = paymentMethods)
      })
    });
    this.stripeService.listConnectedAccounts().subscribe(response => this.connectedAccounts = response);
  }

  //TODO:// form validation
  onSubmit() {
    let requestInfo = Object.assign(new Charge(), this.chargeInfo);
    requestInfo.amount = (requestInfo.amount || 0) * 100; //precision!!!!!!
    this.stripeService.charge(requestInfo).subscribe(response => {
      console.log('The charge response is ');
      console.log(response);

      this.router.navigate(['/charge/' + response.id]);
    });
  }

  objectKeys(obj: any) {
    return Object.keys(obj);
  }
}
