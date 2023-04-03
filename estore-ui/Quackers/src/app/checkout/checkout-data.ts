import { Duck } from "../duck";
import { Cart } from "../shopping-cart";

export interface CheckoutData {
    cart: Cart,
    customDucks: Duck[]
}