import { Component, OnInit } from '@angular/core';
import {ConnectedAccount} from "../model/connected-account";
import {StripeService} from "../service/stripe.service";

@Component({
  selector: 'app-connected-account',
  templateUrl: './connected-account.component.html',
  styleUrls: ['./connected-account.component.scss']
})
export class ConnectedAccountComponent implements OnInit {
  connectedAccounts: Array<ConnectedAccount> = [];

  constructor(private stripeService: StripeService) { }

  ngOnInit(): void {
    this.stripeService.listConnectedAccounts().subscribe(response => this.connectedAccounts = response);
  }

  addNewConnectedAccount() {
    this.stripeService.createConnectedAccount().subscribe(response => {
      this.stripeService.listConnectedAccounts().subscribe(response => this.connectedAccounts = response);
    });
  }
}
