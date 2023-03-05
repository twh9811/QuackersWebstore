package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.ShoppingCart;
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

@Tag("Controller-tier")
public class ShoppingCartControllerTest {
    private ShoppingCartController cartController;
    private ShoppingCartDAO mockCartDAO;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock CartDAO
     */
    @BeforeEach
    public void setupInventoryController() {
        mockCartDAO = mock(ShoppingCartDAO.class);
        cartController = new ShoppingCartController(mockCartDAO);
    }

    @Test
    public void testGetShoppingCart() throws IOException { // getShoppingCart may throw IOException
        // Setup
        // Duck duck1 = new Duck(99, "Galactic Agent", 10, "9.99", Size.MEDIUM,
        // Colors.BLUE, 0, 0, 0, 0, 0);
        // Duck duck2 = new Duck(11, "Quackers", 10, "0.99", Size.SMALL, Colors.RED, 0,
        // 0, 0, 0, 0);
        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.getShoppingCart(cart.getCustomerId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetShoppingCartNotFound() throws Exception { // createShoppingCart may throw IOException
        // Setup
        int CustomerId = 99;
        // When the customer id is passed in, our mock Cart DAO will return null,
        // simulating
        // no cart found
        when(mockCartDAO.getShoppingCart(CustomerId)).thenReturn(null);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.getShoppingCart(CustomerId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartHandleException() throws Exception { // createShoppingcart may throw IOException
        // Setup
        int CustomerId = 99;
        // When getShoppingCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).getShoppingCart(CustomerId);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.getShoppingCart(CustomerId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test 
    public void testGetShoppingCarts() throws IOException { // getShoppingCarts may throw IOException
        // Setup
        // Duck duck1 = new Duck(99, "Galactic Agent", 10, "9.99", Size.MEDIUM,
        // Colors.BLUE, 0, 0, 0, 0, 0);
        // Duck duck2 = new Duck(11, "Quackers", 10, "0.99", Size.SMALL, Colors.RED, 0,
        // 0, 0, 0, 0);
        HashMap<String, Integer> items_0 = new HashMap<>();
        items_0.put("1", 10);
        items_0.put("2", 20);

        HashMap<String, Integer> items_1 = new HashMap<>();
        items_1.put("1", 10);
        items_1.put("2", 20);

        ShoppingCart[] carts = new ShoppingCart[2];
        carts[0] = new ShoppingCart(0, items_0);
        carts[1] = new ShoppingCart(1, items_1);

        // When getShoppingCarts is called return the Shopping Carts created above
        when(mockCartDAO.getShoppingCarts()).thenReturn(carts);

        // Invoke
        ResponseEntity<ShoppingCart[]> response = cartController.getShoppingCarts();

        // Analyse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
    }

    @Test
    public void testGetShoppingCartsHandleException() throws IOException { // getShoppingCarts may throw IOException
        // Setup
        // When getShoppingCarts is called on the Mock Dao, throw an exception
        doThrow(new IOException()).when(mockCartDAO).getShoppingCarts();

        // Invoke
        ResponseEntity<ShoppingCart[]> response = cartController.getShoppingCarts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}
