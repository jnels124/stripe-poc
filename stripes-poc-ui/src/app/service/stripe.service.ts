import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {PaymentMethod} from "../model/payment-method";
import {Charge} from "../model/charge";
import {ConnectedAccount} from "../model/connected-account";

const API_ROOT = '/api/v1/'
const CUSTOMERS = API_ROOT + "customer"
const CARD = API_ROOT + "paymentmethods/card"
const CHARGE = API_ROOT + "charge"
const CONNECTED_ACCOUNT = API_ROOT + "connectedAccount"

@Injectable({
  providedIn: 'root'
})
export class StripeService {

  constructor(private httpClient: HttpClient) {
  }

  createCustomer(name: string): Observable<any> {
    return this.httpClient.post(CUSTOMERS, {"name": name})
  }

  listCustomers(): Observable<Array<any>> {
    return this.httpClient.get(CUSTOMERS + "/list").pipe(map((response:any) => {
      return response.data;
    }));
  }

  addPaymentMethod(details: PaymentMethod): Observable<any> {
    return this.httpClient.post(CARD, Object.assign({}, details))
  }

  getPaymentMethods(customerId: string): Observable<Array<PaymentMethod>> {
    return this.httpClient.get(CARD + "/list", {params: {customerId: customerId}}).pipe(map((response:any) => {
      return response.data.map((paymentMethod: any) => Object.assign(new PaymentMethod(), paymentMethod));
    }));
  }

  charge(chargeInfo: Charge): Observable<Charge> {
    return this.httpClient.post(CHARGE, Object.assign({}, chargeInfo)).pipe(map((response: any) => {
      return Object.assign(new Charge(), response.charges.data[0]); //todo handle multiple charges
    }));
  }

  createConnectedAccount(): Observable<ConnectedAccount> {
    return this.httpClient.post(CONNECTED_ACCOUNT, {}).pipe(map((response: any) => {
      return Object.assign(new ConnectedAccount(), response);
    }));
  }

  listConnectedAccounts(): Observable<Array<ConnectedAccount>> {
    return this.httpClient.get(CONNECTED_ACCOUNT + "/list").pipe(map((response: any) => {
      return response.data.map((connectedAccount: any) => Object.assign(new ConnectedAccount(), connectedAccount));
    }));
  }

  getChargeDetails(id: string): Observable<Charge> {
    return this.httpClient.get(`${CHARGE}/${id}`).pipe(map((response: any) => {
      return Object.assign(new Charge(), response);
    }));
  }

  getCustomerCharges(customerId: string): Observable<Array<Charge>> {
    return this.httpClient.get(CHARGE, {params: {customerId: customerId}}).pipe(map((response: any) => {
      return response.data.map((charge: any) => Object.assign(new Charge(), charge));
    }));
  }
}
