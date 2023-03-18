package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the ShoppingCart class
 * 
 * @author Mason Bausenwein
 */
@Tag("Model-tier")
public class ShoppingCartTest {

    @Test
    public void testConstructorWithValidMap() {
        int expected_customer_id = 10;
        Map<String, Integer> expected_items = new HashMap<>();

        expected_items.put("1", 1);
        expected_items.put("2", 2);
        expected_items.put("3", 3);

        ShoppingCart cart = new ShoppingCart(expected_customer_id, expected_items);

        assertEquals(expected_customer_id, cart.getCustomerId(), "Customer id is incorrect");
        assertEquals(expected_items.size(), cart.getItems().size(), "Items map has an incorrect size");

        for (String duckId : expected_items.keySet()) {
            assertEquals(expected_items.get(duckId), cart.getItems().get(duckId),
                    "Duck with id " + duckId + " has an incorrect quantity");
        }
    }

    @Test
    public void testConstructorWithInvalidMap() {
        int expected_customer_id = 10;
        Map<String, Integer> expected_items = new HashMap<>();

        expected_items.put("1", 1);
        expected_items.put("2", -2);
        expected_items.put("3", 3);

        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCart(expected_customer_id, expected_items),
                "IllegalArgumentException was not thrown");
    }

    @Test
    public void testConstructorWithoutMap() {
        int expected_customer_id = 10;
        ShoppingCart cart = new ShoppingCart(expected_customer_id);

        assertEquals(expected_customer_id, cart.getCustomerId(), "Customer id is incorrect");
        assertTrue(cart.getItems().isEmpty(), "Items map is not empty");
    }

    @Test
    public void testEquals() {
        int customerId = 10;
        Map<String, Integer> items = new HashMap<>();
        Map<String, Integer> otherItems = new HashMap<>();

        items.put("1", 13);
        items.put("2", 19);
        items.put("3", 21);

        otherItems.put("1", 19);

        ShoppingCart cartOne = new ShoppingCart(customerId, items);
        ShoppingCart cartTwo = new ShoppingCart(customerId, new HashMap<>(items));
        ShoppingCart cartThree = new ShoppingCart(customerId, otherItems);
        ShoppingCart cartFour = new ShoppingCart(customerId - 1, items);
        ShoppingCart cartFive = new ShoppingCart(customerId - 1, otherItems);

        assertEquals(cartOne, cartTwo, "cartOne and cartTwo should be equal but are not");
        assertNotEquals(cartOne, cartThree, "cartOne and cartThree shouldn't be equal but are");
        assertNotEquals(cartOne, cartFour, "cartOne and cartFour shouldn't be equal but are");
        assertNotEquals(cartOne, cartFive, "cartOne and cartFive shouldn't be equal but are");
        assertNotEquals(cartThree, cartFour, "cartThree and cartFour shouldn't be equal but are");
        assertNotEquals(cartThree, cartFive, "cartThree and cartFive shouldn't be equal but are");
        assertNotEquals(cartFour, cartFive, "cartFour and cartFive shouldn't be equal but are");
        assertNotEquals(cartOne, new Object(), "cartOne and Object should not be equal but are");
    }

    @Test
    public void testToString() {
        int customerId = 10;
        Map<String, Integer> items = new HashMap<>();

        items.put("1", 13);
        items.put("2", 19);
        items.put("3", 21);

        ShoppingCart cart = new ShoppingCart(customerId, items);

        // Joins the items list to a string delimited by ', '
        String itemsString = items.entrySet().stream().map(Object::toString).collect(Collectors.joining(", "));
        String expected_toString = String.format(ShoppingCart.FORMAT, customerId, itemsString);
        assertEquals(expected_toString, cart.toString(), "toString is incorrect");
    }

}