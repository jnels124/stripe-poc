import {CardDetails} from "./card-details";

export class PaymentMethod {
  id: string | null = null;
  customer: string | null;
  card: CardDetails = new CardDetails();

  constructor(customerId: string | null = null) {
    this.customer = customerId;
  }
}
