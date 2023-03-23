package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.Colors;
import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.DuckOutfit;
import com.ducks.api.ducksapi.model.ShoppingCart;
import com.ducks.api.ducksapi.model.Size;
import com.ducks.api.ducksapi.persistence.DuckDAO;
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

/**
 * Test the Checkout Controller class
 * 
 * @author Mason Bausenwein
 */
@Tag("Controller-tier")
public class CheckoutControllerTest {
    private CheckoutController checkoutController;
    private ShoppingCartDAO mockCartDAO;
    private DuckDAO mockDuckDAO;

    private Duck duckOne;
    private Duck duckTwo;
    private Duck duckThree;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock Duck DAO
     */
    @BeforeEach
    public void setupCheckoutController() throws IOException {
        mockCartDAO = mock(ShoppingCartDAO.class);
        mockDuckDAO = mock(DuckDAO.class);
        checkoutController = new CheckoutController(mockCartDAO, mockDuckDAO);

        Map<String, Integer> items = new HashMap<>();
        items.put("1", 1);
        items.put("2", 2);
        items.put("3", 3);

        ShoppingCart cart = new ShoppingCart(0, items);
        // When the same id is passed in, our mock Cart DAO will return the cart object
        when(mockCartDAO.getShoppingCart(cart.getCustomerId())).thenReturn(cart);

        duckOne = new Duck(1, "Cool duck", 10, 0.99, Size.LARGE, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));
        duckTwo = new Duck(2, "Cool duck 2", 2, 0.99, Size.LARGE, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));
        duckThree = new Duck(3, "Cool duck 2", 5, 0.99, Size.LARGE, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));

        when(mockDuckDAO.getDuck(1)).thenReturn(duckOne);
        when(mockDuckDAO.getDuck(2)).thenReturn(duckTwo);
        when(mockDuckDAO.getDuck(3)).thenReturn(duckThree);
    }

    @Test
    public void testCheckout() throws IOException {
        ResponseEntity<ShoppingCart> response = checkoutController.checkout(0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ShoppingCart(0), response.getBody());

        // Check inventory values
        assertEquals(9, duckOne.getQuantity());
        assertEquals(0, duckTwo.getQuantity());
        assertEquals(2, duckThree.getQuantity());
    }

    @Test
    public void testCheckoutEmpty() throws IOException {
        when(mockCartDAO.getShoppingCart(0)).thenReturn(new ShoppingCart(0));

        ResponseEntity<ShoppingCart> response = checkoutController.checkout(0);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckoutInvalidItems() throws IOException {
        when(mockDuckDAO.getDuck(1)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = checkoutController.checkout(0);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckoutNotFound() throws IOException {
        when(mockCartDAO.getShoppingCart(0)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = checkoutController.checkout(0);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckoutNullPointerException() throws IOException {
        ShoppingCart cart = mockCartDAO.getShoppingCart(0);
        CheckoutController spyController = spy(checkoutController);

        doReturn(new HashMap<>()).when(spyController).getInvalidItems(cart);
        when(mockDuckDAO.getDuck(3)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = spyController.checkout(0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckoutNumberFormatException() throws IOException {
        ShoppingCart cart = mockCartDAO.getShoppingCart(0);
        CheckoutController spyController = spy(checkoutController);

        cart.setItems(Map.of("1", 1, "a", 2, "3", 2));

        doReturn(new HashMap<>()).when(spyController).getInvalidItems(cart);

        ResponseEntity<ShoppingCart> response = spyController.checkout(0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckoutIOException() throws IOException {
        doThrow(new IOException()).when(mockCartDAO).getShoppingCart(0);

        ResponseEntity<ShoppingCart> response = checkoutController.validateCart(0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testValidateItems() throws IOException {
        duckTwo.setQuantity(1);
        when(mockDuckDAO.getDuck(3)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = checkoutController.validateCart(0);

        Map<String, Integer> expectedItems = new HashMap<>();
        expectedItems.put("1", 1);
        expectedItems.put("2", 1);

        ShoppingCart expectedCart = new ShoppingCart(0, expectedItems);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCart, response.getBody());
    }

    @Test
    public void testValidateNoInvalidItems() throws IOException {
        ResponseEntity<ShoppingCart> response = checkoutController.validateCart(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testValidateCartNotFound() throws IOException {
        when(mockCartDAO.getShoppingCart(0)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = checkoutController.validateCart(0);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testValidateIOException() throws IOException {
        doThrow(new IOException()).when(mockCartDAO).getShoppingCart(0);

        ResponseEntity<ShoppingCart> response = checkoutController.validateCart(0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetInvalidItemsNoItems() throws IOException {
        Map<String, Duck> actual = checkoutController.getInvalidItems(new ShoppingCart(0));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetInvalidItemsIntegerNaN() throws IOException {
        ShoppingCart cart = new ShoppingCart(0, Map.of("a", 1));

        Map<String, Duck> actual = checkoutController.getInvalidItems(cart);
        assertEquals(1, actual.size());
        assertNull(actual.get("a"));
    }

}
