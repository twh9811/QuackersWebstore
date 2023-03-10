package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.ShoppingCart;
import com.ducks.api.ducksapi.persistence.DuckDAO;
import com.ducks.api.ducksapi.persistence.DuckFileDAO;
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

/**
 * Handles the REST API requests for the Shopping Cart resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Beining Zhou, Andrew Le
 */

@Tag("Controller-tier")
public class ShoppingCartControllerTest {
    private ShoppingCartController cartController;
    private ShoppingCartDAO mockCartDAO;
    private DuckDAO mockDuckDAO;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock CartDAO
     */
    @BeforeEach
    public void setupInventoryController() {
        mockCartDAO = mock(ShoppingCartDAO.class);
        mockDuckDAO = mock(DuckFileDAO.class);
        cartController = new ShoppingCartController(mockCartDAO, mockDuckDAO);
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
    public void testGetShoppingCartNotFound() throws Exception { // getShoppingCart may throw IOException
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
    public void testGetShoppingCartsNoCarts() throws IOException{
        // When getShoppingCarts is called return the Shopping Carts created above
        when(mockCartDAO.getShoppingCarts()).thenReturn(null);

        // Invoke
        ResponseEntity<ShoppingCart[]> response = cartController.getShoppingCarts();

        // Analyse
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartsHandleException() throws IOException { // getShoppingCarts may throw IOException
        // Setup
        // When getShoppingCarts is called on the Mock Dao, throw an exception
        doThrow(new IOException()).when(mockCartDAO).getShoppingCarts();

        // Invoke
        ResponseEntity<ShoppingCart[]> response = cartController.getShoppingCarts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateShoppingCart() throws IOException { // createShoppingCart may throw IOException
        // Setup
        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);

        // when createShoppingCart is called, return true simulating successful
        // creation and save
        when(mockCartDAO.createShoppingCart(cart)).thenReturn(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testCreateShoppingCartFailed() throws IOException { // createShoppingCart may throw IOException
        // Setup
        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateShoppingCartHandleException() throws IOException { // createShoppingCart may throw IOException
        // Setup
        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);

        doThrow(new IOException()).when(mockCartDAO).createShoppingCart(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartPrice() throws IOException {
        Duck duckOne = new Duck(1, "Cool Duck", 10, "0.99", null, null, null);
        Duck duckTwo = new Duck(1, "Cool Duck 2", 10, "1.99", null, null, null);

        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);
        when(mockDuckDAO.getDuck(1)).thenReturn(duckOne);
        when(mockDuckDAO.getDuck(2)).thenReturn(duckTwo);

        // Invoke
        ResponseEntity<String> response = cartController.getShoppingCartPrice(cart.getCustomerId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("49.7", response.getBody());
    }

    @Test
    public void testGetShoppingCartPriceInvalidDuck() throws IOException {
        Duck duckOne = new Duck(1, "Cool Duck", 10, "0.99", null, null, null);

        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);
        when(mockDuckDAO.getDuck(1)).thenReturn(duckOne);
        when(mockDuckDAO.getDuck(2)).thenReturn(null);

        // Invoke
        ResponseEntity<String> response = cartController.getShoppingCartPrice(cart.getCustomerId());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetShoppingCartPriceNotFound() throws IOException {
        int customerId = 0;
        when(mockCartDAO.getShoppingCart(customerId)).thenReturn(null);

        // Invoke
        ResponseEntity<String> response = cartController.getShoppingCartPrice(customerId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetShoppingCartPriceException() throws IOException {
        int customerId = 0;

        doThrow(new IOException()).when(mockCartDAO).getShoppingCart(customerId);

        // Invoke
        ResponseEntity<String> response = cartController.getShoppingCartPrice(customerId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetShoppingCartInvalidDuckIds() throws IOException {
        Duck duckOne = new Duck(1, "Cool Duck", 10, "0.99", null, null, null);

        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);
        items.put("3", 20);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);
        when(mockDuckDAO.getDuck(1)).thenReturn(duckOne);
        when(mockDuckDAO.getDuck(2)).thenReturn(null);

        // Invoke
        ResponseEntity<List<String>> response = cartController.getShoppingCartInvalidDuckIds(cart.getCustomerId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("2", "3"), response.getBody());
    }

    @Test
    public void testGetShoppingCartInvalidDuckIdsNoInvalids() throws IOException {
        Duck duckOne = new Duck(1, "Cool Duck", 10, "0.99", null, null, null);
        Duck duckTwo = new Duck(1, "Cool Duck 2", 10, "1.99", null, null, null);

        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);
        when(mockDuckDAO.getDuck(1)).thenReturn(duckOne);
        when(mockDuckDAO.getDuck(2)).thenReturn(duckTwo);

        // Invoke
        ResponseEntity<List<String>> response = cartController.getShoppingCartInvalidDuckIds(cart.getCustomerId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetShoppingCartInvalidDuckIdsNotFound() throws IOException {
        int customerId = 0;
        when(mockCartDAO.getShoppingCart(customerId)).thenReturn(null);

        // Invoke
        ResponseEntity<List<String>> response = cartController.getShoppingCartInvalidDuckIds(customerId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetShoppingCartInvalidDuckIdsException() throws IOException {
        int customerId = 0;

        doThrow(new IOException()).when(mockCartDAO).getShoppingCart(customerId);

        // Invoke
        ResponseEntity<List<String>> response = cartController.getShoppingCartInvalidDuckIds(customerId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateShoppingCart() throws IOException {
        int customerId = 0;

        HashMap<String, Integer> items = new HashMap<>();
        items.put("1", 10);
        items.put("2", 20);

        ShoppingCart cart = new ShoppingCart(customerId, items);
        when(mockCartDAO.updateShoppingCart(cart)).thenReturn(cart);

        cart.removeItems(2);

        ResponseEntity<ShoppingCart> updateReponse = cartController.updateShoppingCart(cart);

        assertEquals(HttpStatus.OK, updateReponse.getStatusCode());
        assertEquals(cart, updateReponse.getBody());
    }

    @Test
    public void testUpdateShoppingCartNotFound() throws IOException {
        ShoppingCart cart = new ShoppingCart(0);
        when(mockCartDAO.updateShoppingCart(cart)).thenReturn(null);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.updateShoppingCart(cart);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateShoppingCartException() throws IOException {
        int customerId = 0;
        ShoppingCart cart = new ShoppingCart(customerId);

        doThrow(new IOException()).when(mockCartDAO).updateShoppingCart(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.updateShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteShoppingCart() throws IOException {
        int customerId = 0;

        when(mockCartDAO.deleteShoppingCart(customerId)).thenReturn(true);

        ResponseEntity<ShoppingCart> response = cartController.deleteShoppingCart(customerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteShoppingCartNotFound() throws IOException {
        int customerId = 0;

        when(mockCartDAO.deleteShoppingCart(customerId)).thenReturn(false);

        ResponseEntity<ShoppingCart> response = cartController.deleteShoppingCart(customerId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteShoppingCartException() throws IOException {
        int customerId = 0;
        doThrow(new IOException()).when(mockCartDAO).deleteShoppingCart(customerId);

        // Invoke
        ResponseEntity<ShoppingCart> response = cartController.deleteShoppingCart(customerId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }


}
