package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * A unit test suite for the abstract Account class.
 * Tests both OwnerAccount and UserAccount classes functionality
 * 
 * @author Travis Hill & Andrew Le
 */
@Tag("Model-tier")
public class AccountTest {

    @Test
    public void testConstructorPlainPassword() {
        // Setup
        int expectedID = 1;
        String expectedUsername = "admin";
        String expectedPassword = "password123";
        boolean expectedAdminStatus = false;
        String expectedFirstName = "";
        String expectedLastName = "";
        String expectedAddress = "";
        String expectedCity = "";
        String expectedZipCode = "";

        // Invoke
        Account account = new UserAccount(expectedID, expectedUsername, expectedPassword);

        //Analyze

        assertEquals(expectedID, account.getId());
        assertEquals(expectedUsername, account.getUsername());
        assertEquals(expectedPassword, account.getPlainPassword());
        assertEquals(expectedID, account.getId());
        assertEquals(expectedAdminStatus, account.getAdminStatus());
        assertEquals(expectedFirstName, account.getFirstName());
        assertEquals(expectedLastName, account.getLastName());
        assertEquals(expectedAddress, account.getAddress());
        assertEquals(expectedCity, account.getCity());
        assertEquals(expectedZipCode, account.getZipCode());
    }

    @Test
    public void testConstructorAdmin() {
        // Setup
        int expectedID = 0;
        String expectedUsername = "admin";
        String expectedPassword = "admin";
        boolean expectedAdminStatus = true;
        String expectedFirstName = "";
        String expectedLastName = "";
        String expectedAddress = "";
        String expectedCity = "";
        String expectedZipCode = "";

        // Invoke
        Account account = new OwnerAccount();

        //Analyze

        assertEquals(expectedID, account.getId());
        assertEquals(expectedUsername, account.getUsername());
        assertEquals(expectedPassword, account.getPlainPassword());
        assertEquals(expectedID, account.getId());
        assertEquals(expectedAdminStatus, account.getAdminStatus());
        assertEquals(expectedFirstName, account.getFirstName());
        assertEquals(expectedLastName, account.getLastName());
        assertEquals(expectedAddress, account.getAddress());
        assertEquals(expectedCity, account.getCity());
        assertEquals(expectedZipCode, account.getZipCode());
    }

    @Test
    public void testSetUsername() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedUsername = "notadmin";
          // Invoke
          account.setUsername(expectedUsername);

          //Analyze
          assertEquals(expectedUsername, account.getUsername());;
    }

    @Test
    public void testSetPassword() {
          // Setup
          int expectedID = 1;
          String expectedUsername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, expectedUsername, plainPassword);

          String expectedPassword = "password";
          // Invoke
          account.setPassword(expectedPassword);
          //Analyze
  
          assertEquals(expectedPassword, account.getPlainPassword());
    }

    @Test
    public void testSetAdminStatusSuccess() {
          // Setup
          Account admin = new OwnerAccount();
          boolean expectedAdminStatusAfterChange = false;

          // Invoke
          admin.setAdminStatus(expectedAdminStatusAfterChange);
          //Analyze
  
          assertEquals(expectedAdminStatusAfterChange, admin.getAdminStatus());
    }

    @Test
    public void testSetAdminStatusFail() {
          // Setup
          int expectedID = 1;
          String originalusername = "notadmin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          boolean attemptedStatus = true;
          boolean actualStatus = false;

          // Invoke
          account.setAdminStatus(attemptedStatus);
          //Analyze
  
          assertEquals(actualStatus, account.getAdminStatus());
    }

    @Test
    public void testToStringRegular() {
        // Setup
        int expectedID = 1;
        String expectedUsername = "notadmin";
        String plainPassword = "password123";
        
        Account account = new UserAccount(expectedID, expectedUsername, plainPassword);
        
        String expectedString = expectedUsername + ":" + account.getPlainPassword();
        // Invoke
        String actual_string = account.toString();
        //Analyze
          
        assertEquals(expectedString, actual_string);
    }

    @Test
    public void testToStringAdmin() {
        // Setup
        Account account = new OwnerAccount();
        
        String expectedString = "admin:" + account.getPlainPassword();
        // Invoke
        String actual_string = account.toString();
        //Analyze
          
        assertEquals(expectedString, actual_string);
    }

    @Test
    public void testEqualsUser() {
        // Setup
        int id1 = 1;
        String user1 = "notadmin";
        String pass1 = "password123";
        
        Account account = new UserAccount(id1, user1, pass1);

        int id3 = 1;
        String user3 = "notadmin1";
        String pass3 = "password123";
        
        Account diffUsername = new UserAccount(id3, user3, pass3);

        int id4 = 1;
        String user4 = "notadmin";
        String pass4 = "password1234";
        
        Account diffPassword = new UserAccount(id4, user4, pass4);
    
        // Invoke
        boolean successTest = account.equals(account);
        boolean diffUsernameFail = account.equals(diffUsername);
        boolean diffPasswordFail = account.equals(diffPassword);
        //Analyze
          
        assertTrue(successTest);
        assertFalse(diffUsernameFail);
        assertFalse(diffPasswordFail);
    }

    @Test
    public void testWeakPassword() {
        // Setup
        int id1 = 1;
        String user1 = "test";
        String pass1 = "weakpassword";
        Account account = new UserAccount(id1, user1, pass1);

        // Invoke
        boolean weakPassword = account.validateStrongPassword(account.getPlainPassword());
        //Analyze
          
        assertFalse(weakPassword);
    }

    @Test
    public void testStrongPassword() {
        // Setup
        int id1 = 1;
        String user1 = "test";
        String pass1 = "ThisIsAStrongPassword123";
        Account account = new UserAccount(id1, user1, pass1);

        // Invoke
        boolean strongPassword = account.validateStrongPassword(account.getPlainPassword());
        //Analyze
          
        assertTrue(strongPassword);
    }

    @Test
    public void testStrongPasswordWithSpecialChars() {
        // Setup
        int id1 = 1;
        String user1 = "test";
        String pass1 = "ThisIsAStrongPassword123!?";
        Account account = new UserAccount(id1, user1, pass1);

        // Invoke
        boolean strongPassword = account.validateStrongPassword(account.getPlainPassword());
        //Analyze
          
        assertTrue(strongPassword);
    }

    @Test
    public void testSetFirstName() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedFirstName = "notBlank";
          // Invoke
          account.setFirstName(expectedFirstName);

          //Analyze
          assertEquals(expectedFirstName, account.getFirstName());;
    }

    @Test
    public void testSetLastName() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedLastName = "notBlank";
          // Invoke
          account.setLastName(expectedLastName);

          //Analyze
          assertEquals(expectedLastName, account.getLastName());;
    }

    @Test
    public void testSetAddress() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedAddress = "notBlank";
          // Invoke
          account.setAddress(expectedAddress);

          //Analyze
          assertEquals(expectedAddress, account.getAddress());;
    }

    @Test
    public void testSetCity() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedCity = "notBlank";
          // Invoke
          account.setCity(expectedCity);

          //Analyze
          assertEquals(expectedCity, account.getCity());;
    }

    @Test
    public void testSetZipCode() {
          // Setup
          int expectedID = 1;
          String originalusername = "admin";
          String plainPassword = "password123";

          Account account = new UserAccount(expectedID, originalusername, plainPassword);

          String expectedZipCode = "notBlank";
          // Invoke
          account.setZipCode(expectedZipCode);

          //Analyze
          assertEquals(expectedZipCode, account.getZipCode());;
    }
    
}
