import { Duck } from "./duck"

export interface Cart {
    customerId: number;
    items: Map<string, number>
  }