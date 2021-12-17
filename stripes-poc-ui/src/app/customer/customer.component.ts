import { Component, OnInit } from '@angular/core';
import {StripeService} from "../service/stripe.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {
  showNewForm: boolean = false;
  newCustomerName: string = '';
  customerList: Array<any> = [];

  constructor(private stripeService: StripeService, private router: Router) { }

  ngOnInit(): void {
   this.stripeService.listCustomers().subscribe(response => this.customerList = response)
   //  this.customerList = [{name: "foo", id: "bar"}];
  }

  onSubmit() {
    console.log('Submitted ' + this.newCustomerName)
    this.stripeService.createCustomer(this.newCustomerName)
      .subscribe(response => {
        console.log('The response is');
        console.log(response);
        this.stripeService.listCustomers().subscribe(response => {
          this.showNewForm = false;
          this.customerList = response;
        });
      })
  }

  goToCustomerDetail(id: string) {
    this.router.navigate(['/customer/' + id]);
  }
}
