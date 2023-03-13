package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

    // TODO: Add testEquals unit test

    private Duck testDuck;
    private Duck testDuckTwo;

    @BeforeEach
    public void setupDuckTest() {
        int expected_id = 99;
        int expected_quantity = 10;
        String expected_price = "9.99";
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

        DuckOutfit outfitTwo = new DuckOutfit(expected_hat_uid, expected_shirt_uid, expected_shoes_uid,
                expected_handitem_uid, expected_jewelry_uid);
        // Invoke
        testDuck = new Duck(expected_id, expected_name, expected_quantity, expected_price, expected_size,
                expected_color, outfitOne);

        testDuckTwo = new Duck(expected_id, expected_name, expected_quantity, expected_price, expected_size,
                expected_color, outfitTwo);
    }

    @Test
    public void testConstructor() {
        // Setup
        int expected_id = 99;
        int expected_quantity = 10;
        String expected_price = "9.99";
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
        String expected_price = "19.99";
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
    public void testDuckEquals()
            throws IllegalArgumentException, IllegalAccessException {

        // Equal Ducks
        assertEquals(testDuck, testDuckTwo);

        testAllEqualPossibilitiesExceptFinal(testDuck, testDuckTwo);

        // Different Id (Id is final)
        Duck diffId = new Duck(testDuck.getId() - 1, testDuck.getName(), testDuck.getQuantity(), testDuck.getPrice(),
                testDuck.getSize(), testDuck.getColor(), testDuck.getOutfit());

        assertNotEquals(testDuck, diffId);
    }

    @Test
    public void testDuckOutfitEquals() throws IllegalArgumentException, IllegalAccessException {
        testAllEqualPossibilitiesExceptFinal(testDuck.getOutfit(), testDuckTwo.getOutfit());
    }

    @Test
    public void testDuckOutfitToString() {
        DuckOutfit outfit = testDuck.getOutfit();
        
        // Setup
        String expected_string = String.format(DuckOutfit.STRING_FORMAT, outfit.getHatUID(), outfit.getShirtUID(),
                outfit.getShoesUID(), outfit.getHandItemUID(), outfit.getJewelryUID());

        // InvokDe
        String actual_string = outfit.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }

    /**
     * Tests the equals functionality by changing one field, testing equality, then
     * resetting it
     * Does not work for final fields
     * 
     * @param <T>      The type of the object being tested
     * @param expected The expected object (not modified)
     * @param actual   The actual object (modified but reset) must be equal to
     *                 expected by equals definition
     * @throws IllegalArgumentException If a field's value is attempted to be set to
     *                                  an illegal value (I.e. an int being set to
     *                                  null)
     * @throws IllegalAccessException   If a field attempting to be accessed does
     *                                  not exist
     */
    private <T> void testAllEqualPossibilitiesExceptFinal(T expected, T actual)
            throws IllegalArgumentException, IllegalAccessException {
        // Tests equality of equivalent objects
        assertEquals(expected, actual);
        // Tests equality of expected to a type other than <T>
        assertNotEquals(expected, new Object());

        Field[] fields = actual.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Gets the type of each field
            Class<?> type = field.getType();

            // Skips any field that is static or final (There's a way to do this with final
            // but quite frankly not worth it)
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            // Allows us to write directly to the field despite it being private
            field.setAccessible(true);

            // Sets the field's value to either -1 or null depending on whether it is a
            // primitive type (i.e. int)
            field.set(actual, type.isPrimitive() ? -1 : null);

            // Checks that the expected and actual objects are not equal
            assertNotEquals(expected, actual);

            // Resets the object
            field.set(actual, field.get(expected));

            // Disables write access to the private fields
            field.setAccessible(false);
        }

    }
}