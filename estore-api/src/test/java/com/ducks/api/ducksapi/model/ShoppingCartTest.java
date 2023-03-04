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
            assertEquals(expected_items.get(duckId), cart.getItemAmount(Integer.parseInt(duckId)),
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
    public void testGetItemsAndCustomerId() {
        int expected_customer_id = 10;
        Map<String, Integer> expected_items = new HashMap<>();

        expected_items.put("1", 1);
        expected_items.put("2", 2);
        expected_items.put("3", 3);

        ShoppingCart cart = new ShoppingCart(expected_customer_id, expected_items);
        assertEquals(expected_customer_id, cart.getCustomerId(), "Customer id is incorrect");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");

        for (String duckId : expected_items.keySet()) {
            String errMsg = "Duck with id " + duckId + " has an incorrect quantity";
            assertEquals(expected_items.get(duckId), cart.getItemAmount(Integer.parseInt(duckId)), errMsg);
        }
    }

    @Test
    public void testGetItemAmount() {
        Map<String, Integer> expected_items = new HashMap<>();
        expected_items.put("1", 1);

        Duck duck = new Duck(1, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckTwo = new Duck(2, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);

        ShoppingCart cart = new ShoppingCart(1, expected_items);

        // Valid getItemAmount
        assertEquals(expected_items.get("1"), cart.getItemAmount(1), "Duck 1 by Id's quantity is incorrect");
        assertEquals(expected_items.get("1"), cart.getItemAmount(duck), "Duck 1 by Obj's quantity is incorrect");

        // Invalid getItemAmount
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(2),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(duckTwo),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount((Duck) null),
                "IllegalArgumentException was not thrown");
    }

    @Test
    public void testAddItems() {
        Map<Integer, Integer> expected_items = new HashMap<>();
        ShoppingCart cart = new ShoppingCart(1);

        Duck duckOne = new Duck(1, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckTwo = new Duck(2, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckThree = new Duck(3, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);

        // addItems - Id
        expected_items.put(1, 1);
        cart.addItems(1);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(1), cart.getItemAmount(1), "Duck 1's quantity is incorrect");

        // addItems - Duck
        expected_items.put(1, 2);
        cart.addItems(duckOne);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(1), cart.getItemAmount(duckOne), "Duck 1's quantity is incorrect");

        // addItems - Multiple Ids
        expected_items.put(1, 3);
        expected_items.put(2, 1);
        expected_items.put(3, 1);
        cart.addItems(1, 2, 3);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(1), cart.getItemAmount(1), "Duck 1's quantity is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(2), "Duck 2's quantity is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(3), "Duck 3's quantity is incorrect");

        // addItems - Multiple Ducks
        expected_items.put(1, 4);
        expected_items.put(2, 2);
        expected_items.put(3, 2);
        cart.addItems(duckOne, duckTwo, duckThree);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(1), cart.getItemAmount(duckOne), "Duck 1's quantity is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(duckTwo), "Duck 2's quantity is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(duckThree), "Duck 3's quantity is incorrect");

        // addItems - Array with size 0
        assertThrows(IllegalArgumentException.class, () -> cart.addItems((Duck) null),
                "IllegalArgumentException was not thrown");
    }

    @Test
    public void testAddItemAmount() {
        Map<Integer, Integer> expected_items = new HashMap<>();
        ShoppingCart cart = new ShoppingCart(1);

        Duck duckTwo = new Duck(2, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);

        // addItemAmount - Id
        expected_items.put(1, 6);
        cart.addItemAmount(1, 6);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(1), cart.getItemAmount(1), "Duck 1's quantity is incorrect");

        // addItemAmount - Duck
        expected_items.put(2, 8);
        cart.addItemAmount(duckTwo, 8);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(duckTwo), "Duck 2's quantity is incorrect");

        // addItemAmount - Id - Negative & 0 Quantity
        assertThrows(IllegalArgumentException.class, () -> cart.addItemAmount(3, -10),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.addItemAmount(3, 0),
                "IllegalArgumentException was not thrown");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(2), "Duck 3's quantity is incorrect");

        // addItemAmount - Duck - Null
        assertThrows(IllegalArgumentException.class, () -> cart.addItemAmount((Duck) null, 100),
                "IllegalArgumentException was not thrown");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(duckTwo), "Duck 3's quantity is incorrect");
    }

    @Test
    public void testRemoveItems() {
        Map<Integer, Integer> expected_items = new HashMap<>();
        ShoppingCart cart = new ShoppingCart(1);

        Duck duckOne = new Duck(1, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckTwo = new Duck(2, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckThree = new Duck(3, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);

        cart.addItemAmount(duckOne, 10);
        cart.addItemAmount(duckTwo, 20);
        cart.addItemAmount(duckThree, 30);

        expected_items.put(1, 10);
        expected_items.put(2, 20);
        expected_items.put(3, 30);

        // removeItems - Id
        expected_items.remove(1);
        cart.removeItems(1);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(1),
                "IllegalArgumentException was not thrown");

        // removeItems - Duck
        expected_items.remove(2);
        cart.removeItems(duckTwo);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(duckTwo),
                "IllegalArgumentException was not thrown");

        // Reset Items
        cart.clearItems();
        cart.addItemAmount(duckOne, 10);
        cart.addItemAmount(duckTwo, 20);
        cart.addItemAmount(duckThree, 30);

        expected_items.put(1, 10);
        expected_items.put(2, 20);
        expected_items.put(3, 30);

        // removeItems - Multiple Ids
        expected_items.remove(1);
        expected_items.remove(3);
        cart.removeItems(1, 3);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(2), "Duck 2's quantity is incorrect");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(1),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(3),
                "IllegalArgumentException was not thrown");

        // Reset Items
        cart.clearItems();
        cart.addItemAmount(duckOne, 10);
        cart.addItemAmount(duckTwo, 20);
        cart.addItemAmount(duckThree, 30);

        expected_items.put(1, 10);
        expected_items.put(2, 20);
        expected_items.put(3, 30);

        // removeItems - Multiple Ducks
        expected_items.put(1, 6);
        expected_items.put(2, 18);
        expected_items.remove(1);
        expected_items.remove(2);
        cart.removeItems(duckOne, duckTwo);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(3), "Duck 3's quantity is incorrect");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(duckOne),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(duckTwo),
                "IllegalArgumentException was not thrown");

        // removeItems - Array with size 0
        assertThrows(IllegalArgumentException.class, () -> cart.removeItems((Duck) null),
                "IllegalArgumentException was not thrown");

        // removeItems - Invalid Id
        assertThrows(IllegalArgumentException.class, () -> cart.removeItems(-1),
                "IllegalArgumentException was not thrown");
    }

    @Test
    public void testRemoveItemAmount() {
        Map<Integer, Integer> expected_items = new HashMap<>();
        ShoppingCart cart = new ShoppingCart(1);

        Duck duckOne = new Duck(1, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckTwo = new Duck(2, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);
        Duck duckThree = new Duck(3, "Name", 0, "0.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 0);

        cart.addItemAmount(duckOne, 10);
        cart.addItemAmount(duckTwo, 20);
        cart.addItemAmount(duckThree, 30);

        expected_items.put(1, 10);
        expected_items.put(2, 20);
        expected_items.put(3, 30);

        // removeItemAmount - Id
        expected_items.put(3, 22);
        cart.removeItemAmount(3, 8);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(3), "Duck 3's quantity is incorrect");

        // removeItemAmount - Duck
        expected_items.put(2, 11);
        cart.removeItemAmount(duckTwo, 9);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(2), cart.getItemAmount(duckTwo), "Duck 2's quantity is incorrect");

        // removeItemAmount - Id - Negative & 0 Quantity
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemAmount(3, -10),
                "IllegalArgumentException was not thrown");
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemAmount(3, 0),
                "IllegalArgumentException was not thrown");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(3), "Duck 3's quantity is incorrect");

        // removeItemAmount - Id - More than Available
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemAmount(3, 100),
                "IllegalArgumentException was not thrown");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(3), "Duck 3's quantity is incorrect");

        // removeItemAmount - Duck - Null
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemAmount((Duck) null, 100),
                "IllegalArgumentException was not thrown");
        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertEquals(expected_items.get(3), cart.getItemAmount(duckThree), "Duck 3's quantity is incorrect");

        // removeItemAmount - Invalid Id
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemAmount(-1, 100),
                "IllegalArgumentException was not thrown");

        // removeItemAmount - Quantity = Current Quantity
        expected_items.remove(3);
        cart.removeItems(3);
        cart.addItemAmount(3, 30);

        cart.removeItemAmount(3, 30);

        assertEquals(expected_items.size(), cart.getItems().size(), "Cart size is incorrect");
        assertThrows(IllegalArgumentException.class, () -> cart.getItemAmount(3),
                "IllegalArgumentException was not thrown");

    }

    @Test
    public void testItemClear() {
        ShoppingCart cart = new ShoppingCart(1);

        cart.addItems(1, 5, 3, 2);
        cart.clearItems();
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty");
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