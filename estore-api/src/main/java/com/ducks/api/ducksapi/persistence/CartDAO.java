package com.ducks.api.ducksapi.persistence;

import java.io.IOException;



import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.ShoppingCart;

/*
 * Defines the interface for cart object persistence
 *  
 * @author Andrew Le
 */
public interface CartDAO {
    
    ShoppingCart[] getShoppingCarts() throws IOException;

    ShoppingCart[] findShoppingCarts(String containsText) throws IOException;

    ShoppingCart getShoppingCart() throws IOException;

    ShoppingCart createShoppingCart(ShoppingCart cart) throws IOException;

    ShoppingCart updateShoppingCart(ShoppingCart cart) throws IOException;

    boolean deleteShoppingCart() throws IOException;
}
