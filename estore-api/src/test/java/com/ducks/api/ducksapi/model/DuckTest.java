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

        // Invoke
        Duck duck = new Duck(expected_id,expected_name);

        // Analyze
        assertEquals(expected_id,duck.getId());
        assertEquals(expected_name,duck.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Duck duck = new Duck(id,name);

        String expected_name = "Galactic Agent";

        // Invoke
        duck.setName(expected_name);

        // Analyze
        assertEquals(expected_name,duck.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        String expected_string = String.format(Duck.STRING_FORMAT,id,name);
        Duck duck = new Duck(id,name);

        // Invoke
        String actual_string = duck.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}