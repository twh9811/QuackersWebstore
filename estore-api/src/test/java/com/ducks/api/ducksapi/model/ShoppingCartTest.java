package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
    public void testConstructorWithArrayList() {
        int expected_customer_id = 10;
        ArrayList<Duck> expected_items = new ArrayList<>();

        Duck duckOne = new Duck(1, "test1", 10, "9.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 1);
        Duck duckTwo = new Duck(2, "test2", 11, "19.99", Size.LARGE, Colors.BLUE, 1, 1, 0, 0, 1);
        Duck duckThree = new Duck(3, "test3", 12, "29.99", Size.LARGE, Colors.BLUE, 2, 0, 3, 0, 1);

        expected_items.add(duckOne);
        expected_items.add(duckTwo);
        expected_items.add(duckThree);

        ShoppingCart cart = new ShoppingCart(expected_customer_id, expected_items);

        assertEquals(expected_customer_id, cart.getCustomerId(), "Customer id is incorrect");
        assertEquals(expected_items, cart.getItems(), "Items arraylist is incorrect");
    }

    @Test
    public void testConstructorWithoutArrayList() {
        int expected_customer_id = 10;

        ShoppingCart cart = new ShoppingCart(expected_customer_id);

        assertEquals(expected_customer_id, cart.getCustomerId(), "Customer id is incorrect");
        assertTrue(cart.getItems().isEmpty(), "Items arraylist is not empty");
    }

    @Test
    public void testItemModifierMethods() {
        int expected_customer_id = 10;
        ArrayList<Duck> expected_items = new ArrayList<>();

        Duck duckOne = new Duck(1, "test1", 10, "9.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 1);
        Duck duckTwo = new Duck(2, "test2", 11, "19.99", Size.LARGE, Colors.BLUE, 1, 1, 0, 0, 1);
        Duck duckThree = new Duck(3, "test3", 12, "29.99", Size.LARGE, Colors.BLUE, 2, 0, 3, 0, 1);

        expected_items.add(duckOne);

        ShoppingCart cart = new ShoppingCart(expected_customer_id, expected_items);

        assertEquals(expected_items, cart.getItems(), "Items arraylist is incorrect");

        // addItems single test
        cart.addItems(duckTwo);
        expected_items.add(duckTwo);
        assertEquals(expected_items, cart.getItems(), "addItems single failed");

        // removeItems single test
        cart.removeItems(duckOne);
        expected_items.remove(duckOne);
        assertEquals(expected_items, cart.getItems(), "removeItems single failed");

        // removeItemById - No Error
        cart.removeItemById(2);
        expected_items.remove(duckTwo);
        assertEquals(expected_items, cart.getItems(), "removeItemById failed");

        // removeItemById - Error
        assertThrows(NullPointerException.class, () -> cart.removeItemById(0), "removeItemById did not throw a NullPointerException");
        assertEquals(expected_items, cart.getItems(), "removeItemById removed an item despite erroring");

        // clearItems test
        cart.clearItems();
        expected_items.clear();
        assertEquals(expected_items, cart.getItems(), "clearItems failed");

        // addItems multiple test
        cart.addItems(duckOne, duckTwo, duckThree);
        expected_items.add(duckOne);
        expected_items.add(duckTwo);
        expected_items.add(duckThree);
        assertEquals(expected_items, cart.getItems(), "addItems multiple failed");

        // removeItems multiple test
        cart.removeItems(duckOne, duckThree);
        expected_items.remove(duckOne);
        expected_items.remove(duckThree);
        assertEquals(expected_items, cart.getItems(), "removeItems multiple failed");
    }

    @Test
    public void testEquals() {
        int customerId = 10;
        ArrayList<Duck> items = new ArrayList<>();
        ArrayList<Duck> otherItems = new ArrayList<>();

        Duck duckOne = new Duck(1, "test1", 10, "9.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 1);
        Duck duckTwo = new Duck(2, "test2", 11, "19.99", Size.LARGE, Colors.BLUE, 1, 1, 0, 0, 1);
        Duck duckThree = new Duck(3, "test3", 12, "29.99", Size.LARGE, Colors.BLUE, 2, 0, 3, 0, 1);

        items.add(duckOne);
        items.add(duckTwo);
        items.add(duckThree);

        otherItems.add(duckOne);

        ShoppingCart cartOne = new ShoppingCart(customerId, items);
        ShoppingCart cartTwo = new ShoppingCart(customerId, new ArrayList<>(items));
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
    }

    @Test
    public void testToString() {
        int customerId = 10;
        ArrayList<Duck> items = new ArrayList<>();

        Duck duckOne = new Duck(1, "test1", 10, "9.99", Size.LARGE, Colors.BLUE, 0, 0, 0, 0, 1);
        Duck duckTwo = new Duck(2, "test2", 11, "19.99", Size.LARGE, Colors.BLUE, 1, 1, 0, 0, 1);
        Duck duckThree = new Duck(3, "test3", 12, "29.99", Size.LARGE, Colors.BLUE, 2, 0, 3, 0, 1);

        items.add(duckOne);
        items.add(duckTwo);
        items.add(duckThree);

        ShoppingCart cart = new ShoppingCart(customerId, items);

        // Joins the items list to a string delimited by ', '
        String itemsString = items.stream().map(Duck::toString).collect(Collectors.joining(", "));
        String expected_toString = String.format(ShoppingCart.FORMAT, customerId, itemsString);
        assertEquals(expected_toString, cart.toString(), "toString is incorrect");
    }

}