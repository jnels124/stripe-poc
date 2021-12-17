import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PaymentMethod} from "../model/payment-method";
import {StripeService} from "../service/stripe.service";
import {Charge} from "../model/charge";

@Component({
  selector: 'app-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.scss']
})
export class CustomerDetailComponent implements OnInit {
  id: string = '';
  showNewPaymentMethodForm: boolean = false;
  paymentMethodDetail: PaymentMethod = new PaymentMethod();
  charges: Array<Charge> = [];
  addPaymentMethodError: any = {};
  paymentMethods: Array<PaymentMethod> = [];

  constructor(private route: ActivatedRoute, private stripeService: StripeService, private router: Router) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id') || '';
    this.stripeService.getPaymentMethods(this.id).subscribe(response => this.paymentMethods = response);
    this.stripeService.getCustomerCharges(this.id).subscribe(response => this.charges = response);
  }

  addNewPaymentMethod() {
    this.paymentMethodDetail = new PaymentMethod(this.id);
    this.showNewPaymentMethodForm = true;
  }

  submitForm() {
    this.stripeService.addPaymentMethod(this.paymentMethodDetail)
      .subscribe(response => {
        console.log('add payment method response');
        console.log(response);
        if (response.error) {
          console.log('Foo barred')
          setTimeout(()=> {
            this.addPaymentMethodError = response.error;
            console.log('The response is');
            console.log(this.addPaymentMethodError);
          }, 1000);
        }
        else {
          this.stripeService.getPaymentMethods(this.id).subscribe(response => {
            this.paymentMethods = response;
            this.showNewPaymentMethodForm = false;
          });
        }
      });
  }

  goToChargeDetail(id: string | null) {
    this.router.navigate(['charge/' + id]);
  }
}
