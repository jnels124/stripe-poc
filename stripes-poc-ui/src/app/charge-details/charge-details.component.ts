import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {StripeService} from "../service/stripe.service";
import {Charge} from "../model/charge";

@Component({
  selector: 'app-charge-details',
  templateUrl: './charge-details.component.html',
  styleUrls: ['./charge-details.component.scss']
})
export class ChargeDetailsComponent implements OnInit {
  id: string | null = null;
  chargeDetails: Charge = new Charge();

  constructor(private route: ActivatedRoute, private stripeService: StripeService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id') || '';
    this.stripeService.getChargeDetails(this.id).subscribe((response: Charge) => this.chargeDetails = response);
  }

}
