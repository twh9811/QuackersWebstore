package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the UserAccount class
 * 
 * @author Andrew Le
 */
@Tag("Model-tier")
public class UserAccountTest {

    @Test
    public void testConstructor() {
        // Setup
        int expected_id = 11;
        String expected_username = "andyromede";
        String expected_password = "123456";
        String expected_hashedPassword = "654321";
        boolean expected_adminStatus = false;

        
    }
}
