import {TransferData} from "./transfer-data";

export class Charge {
  id: string | null = null;
  customer: string | null = null;
  payment_method: string | null = null;
  amount: number | null = null;
  transfer_data: TransferData = new TransferData();
  transfer_group: string | null = null;
  transfer: string | null = null;
  receipt_url: string | null = null;
  application_fee_amount: string | null = null;
  currency: string = 'usd';
}
