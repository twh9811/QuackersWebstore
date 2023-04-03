package com.ducks.api.ducksapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.model.OwnerAccount;
import com.ducks.api.ducksapi.model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A unit test suite for the AccountFileDAO Class to test its functionality
 * 
 * @author Travis Hill & Andrew Le
 */
@Tag("Persistence-tier")
public class AccountFileDAOTest {
    AccountFileDAO accountFileDAO;
    Account[] testAccounts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the  from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupAccountFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testAccounts = new Account[4];

        testAccounts[0] = new OwnerAccount();
        testAccounts[1] = new UserAccount(1, "Jeff", "password");
        testAccounts[2] = new UserAccount(2, "Travis", "password");
        testAccounts[3] = new UserAccount(3, "Bob", "password");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the duck array above
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Account[].class))
                .thenReturn(testAccounts);
        accountFileDAO = new AccountFileDAO("doesnt_matter.txt", mockObjectMapper);

    }

    @Test
    public void testGetAccounts() throws IOException {
        // Invoke
        Account[] accounts = accountFileDAO.getAccounts();
        // Analyze
        assertEquals(accounts.length, testAccounts.length);
        for (int i = 0; i < testAccounts.length; i++) {
            assertEquals(accounts[i], testAccounts[i]);
        }
    }

    @Test
    public void testFindAccounts() throws IOException {
        // Invoke
        Account[] accounts = accountFileDAO.findAccounts("Tra");
        Account[] emptySearch = accountFileDAO.findAccounts("Zuch");
        // Analyze
        assertEquals(1, accounts.length);
        assertEquals(testAccounts[2], accounts[0]);
        assertEquals(0, emptySearch.length);
    }

    @Test
    public void testGetAccount() throws IOException {
        // Invoke
        Account account1 = accountFileDAO.getAccount(1);
        Account account2 = accountFileDAO.getAccount(2);
        Account failResult = accountFileDAO.getAccount(100);

        // Analyze
        assertEquals(account1, testAccounts[1]);
        assertEquals(account2, testAccounts[2]);
        assertNull(failResult);
    }

    @Test
    public void testCreateUserAccount() throws IOException {
        // Setup
        Account successAccount = new UserAccount(4, "Timmy", "StrongPassword1234!");
        Account failAccount = new UserAccount(5, "Travis", "password");

        // Invoke
        Account successResult = accountFileDAO.createAccount(successAccount);
        Account failResult = accountFileDAO.createAccount(failAccount);

        //Analyze
        Account createdAccount = accountFileDAO.getAccount(successResult.getId());;
        assertNull(failResult);
        assertEquals(UserAccount.class, successResult.getClass());
        assertEquals(4, createdAccount.getId());
        assertEquals("Timmy", createdAccount.getUsername());
        assertEquals("StrongPassword1234!", createdAccount.getPlainPassword());
        assertEquals("", createdAccount.getFirstName());
        assertEquals("", createdAccount.getLastName());
        assertEquals("", createdAccount.getAddress());
        assertEquals("", createdAccount.getCity());
        assertEquals("", createdAccount.getZipCode());
        assertEquals("", createdAccount.getCard());
        assertEquals("", createdAccount.getExpDate());
        assertEquals(-1, createdAccount.getCVV());
        assertFalse(createdAccount.getAdminStatus());
    }

    @Test
    public void testCreateOwnerAccount() throws IOException {
        // Setup
        Account failAccount = new OwnerAccount();

        // Invoke
        Account failResult = accountFileDAO.createAccount(failAccount);

        //Analyze
        Account createdAccount = accountFileDAO.getAccount(0);

        assertNull(failResult);
        assertEquals(OwnerAccount.class, createdAccount.getClass());
        assertEquals(0, createdAccount.getId());
        assertEquals("admin", createdAccount.getUsername());
        assertEquals("admin", createdAccount.getPlainPassword());
        assertEquals("", createdAccount.getFirstName());
        assertEquals("", createdAccount.getLastName());
        assertEquals("", createdAccount.getAddress());
        assertEquals("", createdAccount.getCity());
        assertEquals("", createdAccount.getZipCode());
        assertEquals("", createdAccount.getCard());
        assertEquals("", createdAccount.getExpDate());
        assertEquals(-1, createdAccount.getCVV());
        assertTrue(createdAccount.getAdminStatus());
    }

    @Test
    public void testUpdateAccount() throws IOException {
        // Setup
        Account updatedAccount = new UserAccount(1, "notJeff", "password");
        Account failUpdatedAccount = new UserAccount(50, "notJeff", "password");

        // Invoke
        Account successResult = accountFileDAO.updateAccount(updatedAccount);
        Account failResult = accountFileDAO.updateAccount(failUpdatedAccount);

        // Analyze
        Account databaseUpdatedAccount = accountFileDAO.getAccount(1);
        assertNull(failResult);
        assertEquals(successResult.getClass(), UserAccount.class);
        assertEquals(databaseUpdatedAccount, updatedAccount);
    }

    @Test
    public void testDeleteAccount() throws IOException {
        // Invoke
        boolean successResult = accountFileDAO.deleteAccount(1);
        boolean failResult = accountFileDAO.deleteAccount(40);
         
        //Analyze
        assertTrue(successResult);
        assertFalse(failResult);
        assertEquals(accountFileDAO.accounts.size(), testAccounts.length - 1);
    }

    @Test
    public void testChangePassword() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newPassword = "Password12345!";
        // Invoke
        boolean successChange = accountFileDAO.changePassword(accountID, "password", newPassword);
        boolean failChangeWrongID = accountFileDAO.changePassword(999, "password", newPassword);
        boolean failChangeWrongOriginalPassword = accountFileDAO.changePassword(accountID, "password123", newPassword);

        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newPassword, updatedAccount.getPlainPassword());
        assertFalse(failChangeWrongID);
        assertFalse(failChangeWrongOriginalPassword);
    }

    @Test
    public void testChangeFirstName() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newFirstName = "Jeff";
        // Invoke
        boolean successChange = accountFileDAO.changeFirstName(accountID, newFirstName);
        boolean failChangeWrongID = accountFileDAO.changeFirstName(999, newFirstName);


        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newFirstName, updatedAccount.getFirstName());
        assertFalse(failChangeWrongID);
    }

    @Test
    public void testChangeLastName() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newLastName = "Baker";
        // Invoke
        boolean successChange = accountFileDAO.changeLastName(accountID, newLastName);
        boolean failChangeWrongID = accountFileDAO.changeLastName(999, newLastName);


        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newLastName, updatedAccount.getLastName());
        assertFalse(failChangeWrongID);
    }

    @Test
    public void testChangeAddress() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newAddress = "1 Memorial Drive";
        // Invoke
        boolean successChange = accountFileDAO.changeAddress(accountID, newAddress);
        boolean failChangeWrongID = accountFileDAO.changeAddress(999, newAddress);


        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newAddress, updatedAccount.getAddress());
        assertFalse(failChangeWrongID);
    }

    @Test
    public void testChangeCity() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newCity = "Rochester";
        // Invoke
        boolean successChange = accountFileDAO.changeCity(accountID, newCity);
        boolean failChangeWrongID = accountFileDAO.changeCity(999, newCity);


        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newCity, updatedAccount.getCity());
        assertFalse(failChangeWrongID);
    }

    @Test
    public void testChangeZipCode() throws IOException {
        // Setup
        Account account = accountFileDAO.getAccount(1);
        int accountID = account.getId();
        String newZipCode = "14586";
        // Invoke
        boolean successChange = accountFileDAO.changeZipCode(accountID, newZipCode);
        boolean failChangeWrongID = accountFileDAO.changeZipCode(999, newZipCode);


        // Analyze
        Account updatedAccount = accountFileDAO.getAccount(accountID);
        assertTrue(successChange);
        assertEquals(newZipCode, updatedAccount.getZipCode());
        assertFalse(failChangeWrongID);
    }

}
