package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Duck class
 * 
 * @author SWEN-261-06, Team 8
 */
@Tag("Model-tier")
public class DuckTest {

    private Duck testDuck;

    @BeforeEach
    public void setupDuckTest() {
        int expected_id = 99;
        int expected_quantity = 10;
        double expected_price = 9.99;
        String expected_name = "Wi-Fire";
        Colors expected_color = Colors.BLUE;
        Size expected_size = Size.SMALL;
        int expected_hat_uid = 0;
        int expected_shirt_uid = 1;
        int expected_shoes_uid = 2;
        int expected_handitem_uid = 3;
        int expected_jewelry_uid = 4;

        DuckOutfit outfitOne = new DuckOutfit(expected_hat_uid, expected_shirt_uid, expected_shoes_uid,
                expected_handitem_uid, expected_jewelry_uid);

        // Invoke
        testDuck = new Duck(expected_id, expected_name, expected_quantity, expected_price, expected_size,
                expected_color, outfitOne);

    }

    @Test
    public void testConstructor() {
        // Setup
        int expected_id = 99;
        int expected_quantity = 10;
        double expected_price = 12.00;
        String expected_name = "Wi-Fire";
        Colors expected_color = Colors.BLUE;
        Size expected_size = Size.SMALL;
        int expected_hat_uid = 0;
        int expected_shirt_uid = 1;
        int expected_shoes_uid = 2;
        int expected_handitem_uid = 3;
        int expected_jewelry_uid = 4;

        // Analyze
        assertEquals(expected_id, testDuck.getId());
        assertEquals(expected_name, testDuck.getName());
        assertEquals(expected_quantity, testDuck.getQuantity());
        assertEquals(expected_price, testDuck.getPrice());
        assertEquals(expected_size, testDuck.getSize());
        assertEquals(expected_color, testDuck.getColor());
        assertEquals(expected_hat_uid, testDuck.getHatUID());
        assertEquals(expected_shirt_uid, testDuck.getShirtUID());
        assertEquals(expected_shoes_uid, testDuck.getShoesUID());
        assertEquals(expected_handitem_uid, testDuck.getHandItemUID());
        assertEquals(expected_jewelry_uid, testDuck.getJewelryUID());
    }

    @Test
    public void testSetAttributes() {
        // Setup
        String expected_name = "Galactic Agent";
        int expected_quantity = 12;
        double expected_price = 19.99;
        Colors expected_color = Colors.RED;
        Size expected_size = Size.MEDIUM;
        int expected_hat_uid = 6;
        int expected_shirt_uid = 7;
        int expected_shoes_uid = 8;
        int expected_handitem_uid = 9;
        int expected_jewelry_uid = 10;

        // Invoke
        testDuck.setName(expected_name);
        testDuck.setQuantity(expected_quantity);
        testDuck.setPrice(expected_price);
        testDuck.setColor(expected_color);
        testDuck.setSize(expected_size);
        testDuck.setHatUID(expected_hat_uid);
        testDuck.setShirtUID(expected_shirt_uid);
        testDuck.setShoesUID(expected_shoes_uid);
        testDuck.setHandItemUID(expected_handitem_uid);
        testDuck.setJewelryUID(expected_jewelry_uid);

        // Analyze
        assertEquals(expected_name, testDuck.getName());
        assertEquals(expected_quantity, testDuck.getQuantity());
        assertEquals(expected_price, testDuck.getPrice());
        assertEquals(expected_color, testDuck.getColor());
        assertEquals(expected_size, testDuck.getSize());
        assertEquals(expected_hat_uid, testDuck.getHatUID());
        assertEquals(expected_shirt_uid, testDuck.getShirtUID());
        assertEquals(expected_shoes_uid, testDuck.getShoesUID());
        assertEquals(expected_handitem_uid, testDuck.getHandItemUID());
        assertEquals(expected_jewelry_uid, testDuck.getJewelryUID());
    }

    @Test
    public void testSetOutfit() {
        // Setup
        DuckOutfit expected = new DuckOutfit(1, 1, 1, 1, 1);

        // Invoke
        testDuck.setOutfit(expected);

        // Analyze
        assertEquals(testDuck.getOutfit(), expected);
    }

    @Test
    public void testDuckToString() {
        // Setup
        String expected_string = String.format(Duck.STRING_FORMAT, testDuck.getId(), testDuck.getName(),
                testDuck.getQuantity(), testDuck.getPrice(), testDuck.getSize(), testDuck.getColor(),
                testDuck.getHatUID(), testDuck.getShirtUID(), testDuck.getShoesUID(), testDuck.getHandItemUID(),
                testDuck.getJewelryUID());

        // InvokDe
        String actual_string = testDuck.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }

    @Test
    public void testDuckInvalid() {
        // All null/-1
        assertThrows(IllegalArgumentException.class, () -> new Duck(1, null, -1, -1, null, null, null));
        // Price empty/DuckOutfit invalid
        assertThrows(IllegalArgumentException.class, () -> new Duck(1, "", -1, 0, null, null, new DuckOutfit(-1, 0, 0, 0, 0)));
        // Price blank
        assertThrows(IllegalArgumentException.class, () -> new Duck(1, " ", -1, 0, null, null, null));
    }

    @Test
    public void testDuckEquals()
            throws IllegalArgumentException, IllegalAccessException {

        Duck expected = new Duck(0, "Duck", 0, 0.00, Size.LARGE, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));
        Duck actual = new Duck(0, "Duck", 0, 0.00, Size.LARGE, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));

        // Test Equal
        assertEquals(expected, actual);

        // Test Not Equal Duck (Everything but Id)
        actual.setName("Duc");
        assertNotEquals(expected, actual);
        actual.setName(expected.getName());

        actual.setQuantity(1);
        assertNotEquals(expected, actual);
        actual.setQuantity(expected.getQuantity());

        actual.setPrice(1.99);
        assertNotEquals(expected, actual);
        actual.setPrice(expected.getPrice());

        actual.setSize(Size.SMALL);
        assertNotEquals(expected, actual);
        actual.setSize(expected.getSize());

        actual.setColor(Colors.GREEN);
        assertNotEquals(expected, actual);
        actual.setColor(expected.getColor());

        actual.setOutfit(new DuckOutfit(1, 0, 0, 0, 0));
        assertNotEquals(expected, actual);

        // Test Not Equal Duck (Different Id - Id is final)
        Duck diffId = new Duck(testDuck.getId() - 1, testDuck.getName(), testDuck.getQuantity(), testDuck.getPrice(),
                testDuck.getSize(), testDuck.getColor(), testDuck.getOutfit());

        assertNotEquals(testDuck, diffId);

        // Test Not Equal (Object)
        assertNotEquals(testDuck, new Object());

    }
}