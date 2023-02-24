package com.ducks.api.ducksapi.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.ShoppingCart;

/*
 * Defines the interface for cart object persistence
 *  
 * @author Andrew Le
 */
public interface CartDAO {
    
    /**
     * Retrieves all {@linkplain Duck items} in {@linkplain ShoppingCart shopping cart}
     * 
     * @param {@linkplain ShoppingCart Shopping cart}
     * 
     * @return An array of {@link Duck duck} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    ArrayList<Duck> getItems(ShoppingCart cart) throws IOException;

    /**
     * adds a {@linkplain Duck duck} to the {@linkplain ShoppingCart shopping cart}
     * 
     * @param {@link Duck duck} object to be added to {@linkplain ShoppingCart shopping cart}
     * @param {@link ShoppingCart Shopping cart object that the duck will be added to}
     * 
     * @return updated {@link ShoppingCart Shopping Cart} if successful, null if
     * {@link ShoppingCart Shopping Cart} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Duck addDuck(Duck duck, ShoppingCart cart) throws IOException;

    



}
