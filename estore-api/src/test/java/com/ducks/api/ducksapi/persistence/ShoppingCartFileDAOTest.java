package com.ducks.api.ducksapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ducks.api.ducksapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test the ShoppingCart File DAO class
 * 
 * @author Mason Bausenwein
 */
@Tag("Persistence-tier")
public class ShoppingCartFileDAOTest {

    ShoppingCartFileDAO cartFileDAO;
    ShoppingCart[] testCarts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupShoppingCartFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testCarts = new ShoppingCart[3];

        ShoppingCart cartOne = new ShoppingCart(0);
        cartOne.addItemAmount(1, 30);
        cartOne.addItemAmount(2, 20);
        cartOne.addItemAmount(3, 10);

        ShoppingCart cartTwo = new ShoppingCart(1);
        cartTwo.addItemAmount(2, 5);
        cartTwo.addItemAmount(3, 3);

        ShoppingCart cartThree = new ShoppingCart(2);
        cartOne.addItemAmount(1, 15);
        cartOne.addItemAmount(3, 13);

        testCarts[0] = cartOne;
        testCarts[1] = cartTwo;
        testCarts[2] = cartThree;

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the duck array above
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), ShoppingCart[].class))
                .thenReturn(testCarts);
        cartFileDAO = new ShoppingCartFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetCarts() {
        // Invoke
        ShoppingCart[] carts = cartFileDAO.getShoppingCarts();

        // Analyze
        assertEquals(carts.length, testCarts.length);

        for (int i = 0; i < testCarts.length; ++i)
            assertEquals(carts[i], testCarts[i], "cart " + i + " was not equal to testCart " + i);
    }

    @Test
    public void testGetValidCart() {
        // Invoke
        ShoppingCart cart = cartFileDAO.getShoppingCart(0);

        // Analyze
        assertEquals(cart, testCarts[0], "cart is not equal to testCarts[0]");
    }

    @Test
    public void testGetInvalidCart() {
        // Invoke
        ShoppingCart cart = cartFileDAO.getShoppingCart(7);

        // Analyze
        assertNull(cart, "cart was not null when it should be");
    }

    @Test
    public void testDeleteValidShoppingCart() {
        boolean didDelete = assertDoesNotThrow(() -> cartFileDAO.deleteShoppingCart(0),
                "Exception was thrown when it shouldn't have");
        assertTrue(didDelete, "didDelete should be true");
        assertEquals(cartFileDAO.carts.size(), testCarts.length - 1);
    }

    @Test
    public void testDeleteInvalidShoppingCart() {
        boolean didDelete = assertDoesNotThrow(() -> cartFileDAO.deleteShoppingCart(7),
                "Exception was thrown when it shouldn't have");
        assertFalse(didDelete, "didDelete should be false");
        assertEquals(cartFileDAO.carts.size(), testCarts.length);
    }

    @Test
    public void testCreateShoppingCart() {
        // Setup
        Map<String, Integer> items = new HashMap<>();
        items.put("1", 10);

        ShoppingCart newCart = new ShoppingCart(3, items);

        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> cartFileDAO.createShoppingCart(newCart),
                "Exception was thrown when it shouldn't have");

        // Analyze
        assertNotNull(result, "The cart resulting from the create operation is null when it shouldn't be");

        ShoppingCart actual = cartFileDAO.getShoppingCart(newCart.getCustomerId());
        assertEquals(actual, newCart, "The actual cart is not equal to newCart when it should be");
    }

    @Test
    public void testCreateDuplicateShoppingCart() {
        // Setup
        int customerId = 1;

        Map<String, Integer> items = new HashMap<>();
        items.put("1", 10);

        ShoppingCart newCart = new ShoppingCart(customerId, items);

        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> cartFileDAO.createShoppingCart(newCart),
                "Exception was thrown when it shouldn't have");

        // Analyze
        assertNull(result, "The cart resulting from the create operation is not null when it should be");

        ShoppingCart actual = cartFileDAO.getShoppingCart(customerId);
        assertNotEquals(actual, newCart, "The actual cart is equal to newCart when it shouldn't be");
    }

    @Test
    public void testValidUpdateCart() {
        // Setup
        int customerId = 1;

        Map<String, Integer> items = new HashMap<>();
        items.put("1", 10);

        ShoppingCart cart = new ShoppingCart(customerId, items);
        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> cartFileDAO.updateShoppingCart(cart),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result, "The result ShoppingCart object should not be null but is");
        ShoppingCart actual = cartFileDAO.getShoppingCart(cart.getCustomerId());
        assertEquals(actual, cart, "The actual and cart objects should be equal but aren't");
    }

    @Test
    public void testInvalidUpdateCart() {
        // Setup
        int customerId = 7;

        Map<String, Integer> items = new HashMap<>();
        items.put("1", 10);

        ShoppingCart cart = new ShoppingCart(customerId, items);
        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> cartFileDAO.updateShoppingCart(cart),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result, "The result ShoppingCart object should be null but is not");
        ShoppingCart actual = cartFileDAO.getShoppingCart(cart.getCustomerId());
        assertNotEquals(actual, cart, "The actual and cart objects should not be equal");
    }
}
