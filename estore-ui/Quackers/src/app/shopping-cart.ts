import { Duck } from "./duck"

export interface Cart {
    id: number;
    items: Map<string, number>
  }