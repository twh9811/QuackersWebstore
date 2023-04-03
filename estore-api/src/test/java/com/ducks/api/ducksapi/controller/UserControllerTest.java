package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.persistence.AccountDAO;
import com.ducks.api.ducksapi.model.UserAccount;

/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Andrew Le, Eric Choi
 */

/**
 * Test the User Controller class
 * 
 * @author Andrew Le, Eric Choi
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private AccountDAO mockAccountDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockAccountDAO = mock(AccountDAO.class);
        userController = new UserController(mockAccountDAO);
    }

    @Test
    public void testCreateUserWeakPassword() throws IOException { // createUser may throw IOException
        // Setup
        Account user = new UserAccount(11, "sam", "123456");

        // when createUser is called, return true simulating successful
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(user);

        // Invoke
        ResponseEntity<Account> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateUserStrongPassword() throws IOException { // createUser may throw IOException
        // Setup
        Account user = new UserAccount(11, "sam", "ThisIsAStrongPassword123!");

        // when createUser is called, return true simulating successful
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(user);

        // Invoke
        ResponseEntity<Account> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException { // createUser may throw IOException
        // Setup
        Account user = new UserAccount(11, "dereck", "654321");
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(null);

        // Invoke
        ResponseEntity<Account> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException { // createUser may throw IOException
        // Setup
        Account user = new UserAccount(11, "eric", "162534");

        // When createUser is called on the Mock Account DAO, throw an IOException
        doThrow(new IOException()).when(mockAccountDAO).createAccount(user);

        // Invoke
        ResponseEntity<Account> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteAccount() throws IOException { // deleteAccount may throw IOException
        // Setup
        int accountId = 11;
        // when deleteAccount is called return true, simulating successful deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenReturn(true);

        // Invoke
        ResponseEntity<Account> response = userController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountNotFound() throws IOException { // deleteAccount may throw IOException
        // Setup
        int accountId = 11;
        // when deleteAccount is called return false, simulating failed deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenReturn(false);

        // Invoke
        ResponseEntity<Account> response = userController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountHandleException() throws IOException { // deleteAccount may throw IOException
        // Setup
        int accountId = 11;
        // when deleteAccount is called return false, simulating failed deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenThrow(new IOException());

        // Invoke
        ResponseEntity<Account> response = userController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLoginUser() throws IOException { // loginUser may throw IO Exception
        // Setup
        Account[] accounts = new Account[1];
        accounts[0] = new UserAccount(11, "sam", "123456");
        // when findAccounts is called, return a list of accounts simulating success
        when(mockAccountDAO.findAccounts(accounts[0].getUsername())).thenReturn(accounts);

        // Invoke
        ResponseEntity<Account> response = userController.loginUser(accounts[0].getUsername(),
                accounts[0].getPlainPassword());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginUserConflict() throws IOException { // loginUser may throw IO Exception
        // Setup
        Account[] accounts = new Account[1];
        accounts[0] = new UserAccount(11, "sam", "123456");
        // when findAccounts is called, return a list of accounts simulating success
        when(mockAccountDAO.findAccounts(accounts[0].getUsername())).thenReturn(accounts);

        // Invoke
        ResponseEntity<Account> response = userController.loginUser(accounts[0].getUsername(), "wrongpassword");

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testLoginUserNotFound() throws IOException { // loginUser may throw IO Exception
        // Setup
        Account[] emptyResult = new Account[0];
        // when findAccounts is called, return a list of accounts simulating success
        when(mockAccountDAO.findAccounts("notsam")).thenReturn(emptyResult);

        // Invoke
        ResponseEntity<Account> response = userController.loginUser("notsam", "fake");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginUserHandleException() throws IOException { // loginUser may throw IO Exception
        // Setup
        Account[] accounts = new Account[1];
        accounts[0] = new UserAccount(11, "sam", "123456");
        // when findAccounts is called, return a list of accounts simulating success
        doThrow(new IOException()).when(mockAccountDAO).findAccounts(accounts[0].getUsername());

        // Invoke
        ResponseEntity<Account> response = userController.loginUser(accounts[0].getUsername(),
                accounts[0].getPlainPassword());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogoutUser() throws IOException { // logoutUser may throw IO Exception
        // Setup
        Account account = new UserAccount(11, "sam", "123456");

        // when updateAccount is called return true, simulating successful update and
        // save
        when(mockAccountDAO.updateAccount(account)).thenReturn(account);

        // invoke
        ResponseEntity<Account> response = userController.logoutUser(account);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testLogoutUserFailed() throws IOException { // logoutUser may throw IO Exception
        // Setup
        Account account = new UserAccount(11, "sam", "123456");
        // when updateAccount is called return true, simulating successful update and
        // save
        when(mockAccountDAO.updateAccount(account)).thenReturn(null);

        // invoke
        ResponseEntity<Account> response = userController.logoutUser(account);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testLogoutUserHandleException() throws IOException { // logoutUser may throw IOException
        // Setup
        Account account = new UserAccount(11, "sam", "123456");
        // When updateAccount is called on the Mock Account DAO, throw an IOException
        doThrow(new IOException()).when(mockAccountDAO).updateAccount(account);

        // Invoke
        ResponseEntity<Account> response = userController.logoutUser(account);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAccount() throws IOException { // getAccount may throw IOException
        // Setup
        Account account = new UserAccount(11, "sam", "123456");
        // When the same id is passed in, our mock Account DAO will return the Account
        // object
        when(mockAccountDAO.getAccount(account.getId())).thenReturn(account);

        // Invoke
        ResponseEntity<Account> response = userController.getAccount(account.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    public void testGetAccountNotFound() throws Exception { // createAccount may throw IOException
        // Setup
        int accountId = 99;
        // When the same id is passed in, our mock Account DAO will return null,
        // simulating
        // no Account found
        when(mockAccountDAO.getAccount(accountId)).thenReturn(null);

        // Invoke
        ResponseEntity<Account> response = userController.getAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAccountHandleException() throws Exception { // createAccount may throw IOException
        // Setup
        int accountId = 99;
        // When getAccount is called on the Mock Account DAO, throw an IOException
        doThrow(new IOException()).when(mockAccountDAO).getAccount(accountId);

        // Invoke
        ResponseEntity<Account> response = userController.getAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testgetAccounts() throws IOException { // getAccounts may throw IOException
        // Setup
        Account[] accounts = new Account[2];
        accounts[0] = new UserAccount(11, "sam", "123456");
        accounts[1] = new UserAccount(11, "eric", "162534");
        // When getAccounts is called return the Accounts created above
        when(mockAccountDAO.getAccounts()).thenReturn(accounts);

        // Invoke
        ResponseEntity<Account[]> response = userController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }

    @Test
    public void testGetAccountsHandleException() throws IOException { // getAccounts may throw IOException
        // Setup
        doThrow(new IOException()).when(mockAccountDAO).getAccounts();

        // Invoke
        ResponseEntity<Account[]> response = userController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAccountsEmpty() throws IOException { // getAccounts may throw IOException
        // Setup
        Account[] accounts = new Account[0];
        when(mockAccountDAO.getAccounts()).thenReturn(accounts);

        // Invoke
        ResponseEntity<Account[]> response = userController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testUpdateAccountStrongPassword() throws IOException {
        // Setup
        Account account = new UserAccount(11, "sam", "Password1");

        when(mockAccountDAO.getAccount(account.getId())).thenReturn(account);
        when(mockAccountDAO.updateAccount(account)).thenReturn(account);

        account.setPassword("Password2");

        // Invoke
        ResponseEntity<Account> response = userController.updateAccount(account);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    public void testUpdateAccountWeakPassword() throws IOException {
        // Setup
        Account account = new UserAccount(11, "sam", "Password1");

        when(mockAccountDAO.getAccount(account.getId())).thenReturn(account);
        when(mockAccountDAO.updateAccount(account)).thenReturn(null);

        account.setPassword("password2");

        // Invoke
        ResponseEntity<Account> response = userController.updateAccount(account);

        // Analyze
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateAccountNotFound() throws IOException {
        // Setup
        Account account = new UserAccount(11, "sam", "Password1");

        when(mockAccountDAO.getAccount(account.getId())).thenReturn(null);

        // Invoke
        ResponseEntity<Account> response = userController.updateAccount(account);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateAccountHandleException() throws IOException { // getAccounts may throw IOException
        // Setup
        Account account = new UserAccount(11, "sam", "Password1");

        when(mockAccountDAO.getAccount(account.getId())).thenReturn(account);
        doThrow(new IOException()).when(mockAccountDAO).updateAccount(account);

        // Invoke
        ResponseEntity<Account> response = userController.updateAccount(account);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}