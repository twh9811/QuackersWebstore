package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.catalina.User;
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
        String expected_plainPassword = "123456";
        String expected_hashedPassword = "654321";
        boolean expected_adminStatus = false;
        
        //invoke
        UserAccount account_one = new UserAccount(expected_id, expected_username, expected_plainPassword);

        UserAccount account_two = new UserAccount(expected_id, expected_username, expected_hashedPassword);
    }
}
