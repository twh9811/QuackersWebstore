package com.ducks.api.ducksapi.persistence;

import java.io.IOException;


import com.ducks.api.ducksapi.model.ShoppingCart;

/*
 * Defines the interface for cart object persistence
 *  
 * @author Andrew Le
 */
public interface ShoppingCartDAO {
    
    /**
     * Retrieves all {@linkplain ShoppingCart shopping cart}
     * 
     * @return An array of {@link ShoppingCart shopping cart} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    ShoppingCart[] getShoppingCarts() throws IOException;


    /**
     * Retrieves a {@linkplain ShoppingCart shopping cart} with the given customer id
     * 
     * @param id The id of the {@link ShoppingCart shopping cart} to get
     * 
     * @return a {@link ShoppingCart shopping cart} object with the matching id
     * <br>
     * null if no {@link ShoppingCart shopping cart} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    ShoppingCart getShoppingCart(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain ShoppingCart shopping cart}
     * 
     * @param cart {@linkplain ShoppingCart shopping cart} object to be created and saved
     * <br>
     * The id of the shopping cart object is ignored and a new uniqe id is assigned
     *
     * @return new {@link ShoppingCart shopping cart} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    ShoppingCart createShoppingCart(ShoppingCart cart) throws IOException;

    /**
     * Updates and saves a {@linkplain ShoppingCart shopping cart}
     * 
     * @param cart {@link ShoppingCart shopping cart} object to be updated and saved
     * 
     * @return updated {@link ShoppingCart shopping cart} if successful, null if
     * {@link ShoppingCart shopping cart} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    ShoppingCart updateShoppingCart(ShoppingCart cart) throws IOException;

    /**
     * Deletes a {@linkplain ShoppingCart shopping cart} with the given customer id
     * 
     * @param id The id of the {@link ShoppingCart shopping cart}
     * 
     * @return true if the {@link ShoppingCart shopping cart} was deleted
     * <br>
     * false if  the {@link ShoppingCart shopping cart} with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteShoppingCart(int id) throws IOException;
}
