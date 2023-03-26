import { Account } from "../account";
import { Cart } from "../shopping-cart";

export interface CheckoutData {
    account: Account;
    cart: Cart;
}