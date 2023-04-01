import { Cart } from "../shopping-cart";

/**
 * Servers a data wrapper to transfer the cart from the checkout page to the receipt prompt
 */
export interface ReceiptData {
    cart: Cart
}