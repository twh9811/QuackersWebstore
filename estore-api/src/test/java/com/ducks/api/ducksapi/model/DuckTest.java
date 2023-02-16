package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ducks.api.ducksapi.model.Duck;

/**
 * The unit test suite for the Duck class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class DuckTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        Colors expected_color = Colors.BLUE;
        Size expected_size = Size.SMALL;
        int expected_hat_uid = 0;
        int expected_shirt_uid = 1;
        int expected_shoes_uid = 2;
        int expected_handitem_uid = 3;
        int expected_jewelry_uid = 4;
        // Invoke
        Duck duck = new Duck(expected_id, expected_name, expected_size, expected_color, expected_hat_uid,
                expected_shirt_uid,
                expected_shoes_uid, expected_handitem_uid, expected_jewelry_uid);

        // Analyze
        assertEquals(expected_id, duck.getId());
        assertEquals(expected_name, duck.getName());
        assertEquals(expected_size, duck.getSize());
        assertEquals(expected_color, duck.getColor());
        assertEquals(expected_hat_uid, duck.getHatUID());
        assertEquals(expected_shirt_uid, duck.getShirtUID());
        assertEquals(expected_shoes_uid, duck.getShoesUID());
        assertEquals(expected_handitem_uid, duck.getHandItemUID());
        assertEquals(expected_jewelry_uid, duck.getJewelryUID());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Colors color = Colors.BLUE;
        Size size = Size.SMALL;
        int hat_uid = 0;
        int shirt_uid = 1;
        int shoes_uid = 2;
        int handitem_uid = 3;
        int jewelry_uid = 4;
        // Invoke
        Duck duck = new Duck(id, name, size, color, hat_uid, shirt_uid, shoes_uid, handitem_uid, jewelry_uid);

        String expected_name = "Galactic Agent";

        // Invoke
        duck.setName(expected_name);

        // Analyze
        assertEquals(expected_name, duck.getName());
    }

    @Test
    public void testSetAttributes() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Colors color = Colors.BLUE;
        Size size = Size.SMALL;
        int hat_uid = 0;
        int shirt_uid = 1;
        int shoes_uid = 2;
        int handitem_uid = 3;
        int jewelry_uid = 4;
        // Invoke
        Duck duck = new Duck(id, name, size, color, hat_uid, shirt_uid, shoes_uid, handitem_uid, jewelry_uid);

        String expected_name = "Galactic Agent";
        Colors expected_color = Colors.RED;
        Size expected_size = Size.MEDIUM;
        int expected_hat_uid = 6;
        int expected_shirt_uid = 7;
        int expected_shoes_uid = 8;
        int expected_handitem_uid = 9;
        int expected_jewelry_uid = 10;

        // Invoke
        duck.setName(expected_name);
        duck.setColor(expected_color);
        duck.setSize(expected_size);
        duck.setHatUID(expected_hat_uid);
        duck.setShirtUID(expected_shirt_uid);
        duck.setShoesUID(expected_shoes_uid);
        duck.setHandItemUID(expected_handitem_uid);
        duck.setJewelryUID(expected_jewelry_uid);

        // Analyze
        assertEquals(expected_name, duck.getName());
        assertEquals(expected_color, duck.getColor());
        assertEquals(expected_size, duck.getSize());
        assertEquals(expected_hat_uid, duck.getHatUID());
        assertEquals(expected_shirt_uid, duck.getShirtUID());
        assertEquals(expected_shoes_uid, duck.getShoesUID());
        assertEquals(expected_handitem_uid, duck.getHandItemUID());
        assertEquals(expected_jewelry_uid, duck.getJewelryUID());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Colors color = Colors.BLUE;
        Size size = Size.SMALL;
        int hat_uid = 0;
        int shirt_uid = 1;
        int shoes_uid = 2;
        int handitem_uid = 3;
        int jewelry_uid = 4;
        String expected_string = String.format(Duck.STRING_FORMAT, id, name, size, color, hat_uid, shirt_uid, shoes_uid,
                handitem_uid, jewelry_uid);
        Duck duck = new Duck(id, name, size, color, hat_uid, shirt_uid, shoes_uid, handitem_uid, jewelry_uid);

        // Invoke
        String actual_string = duck.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
}