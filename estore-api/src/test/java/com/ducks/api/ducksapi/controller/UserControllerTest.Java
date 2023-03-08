package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private UserController accountController;
    private AccountDAO mockAccountDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockAccountDAO = mock(AccountDAO.class);
        accountController = new UserController(mockAccountDAO);
    }

    @Test
    public void testCreateUser() throws IOException { // createUser may throw IOException
        // Setup
        Account user = new UserAccount(11, "sam", "123456");

        // when createUser is called, return true simulating successful
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(user);

        // Invoke
        ResponseEntity<Account> response = accountController.createUser(user);

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
        ResponseEntity<Account> response = accountController.createUser(user);

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
        ResponseEntity<Account> response = accountController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteAccount() throws IOException { // deleteAccount may throw IOException
        // Setup
        int accountId = 11;
        // when deleteHero is called return true, simulating successful deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenReturn(true);

        // Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteAccountNotFound() throws IOException { // deleteAccount may throw IOException
        // Setup
        int accountId = 11;
        // when deleteHero is called return true, simulating successful deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenReturn(true);

        // Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteHeroHandleException() throws IOException { // deleteHero may throw IOException
        // Setup
        int accountId = 11;
        // when deleteHero is called return true, simulating successful deletion
        when(mockAccountDAO.deleteAccount(accountId)).thenReturn(true);

        // Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(accountId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    
}