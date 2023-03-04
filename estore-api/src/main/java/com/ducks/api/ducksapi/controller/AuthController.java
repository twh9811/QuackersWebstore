package com.ducks.api.ducksapi.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ducks.api.ducksapi.persistence.AccountDAO;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.model.UserAccount;

/**
 * 
 * Handles the REST API requests for the Account resources
 * 
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Travis Hill
 */

@RestController
@RequestMapping("login")
public class AuthController {
    private AccountDAO accountDAO;

    /**
     * Creates a REST API controller to reponds to requests specifically for accoount data
     * 
     * @param accountDAO The {@link AccountDAO Account Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
    */
    public AuthController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    @PostMapping("/account")
    public ResponseEntity<Account> createUser(@RequestBody Account account) {
        // GET /login/?username=username&password=password
        try {
            Account newAccount = accountDAO.createAccount(account);
            // Username doesn't already exist in system, OK to create account
            if(newAccount != null) {
                return new ResponseEntity<Account>(newAccount, HttpStatus.OK);
            }
            // Account exists in the system already.
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        // Something went wrong not related to user creation.
        } catch(IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * Responds to the GET request for a {@linkplain Account account} attempting to login to the site
     * 
     * @param username The username of the login attempt
     * @param password The password of the login attempt
     * @return The account in the database if attempt successful
     * 
     * HttpStatus OK if the authentication was successful
     * HttpStatus CONFLICT if the authentication failed
     * HttpStatus NOT_FOUND if the login attempt was for an account was not found in the database
     */
    @GetMapping("/")
    public ResponseEntity<Account> authenticateUser(@RequestParam String username, @RequestParam String password) {
        // GET /login/?username=username&password=password
        try {
            Account[] databaseAccounts = accountDAO.findAccounts(username);
            // This means account does exist in system
            if(databaseAccounts.length != 0) {
                // Create a temporary account. Only one account with the username can exist so ID doesn't matter
                UserAccount tempAccount = new UserAccount(-100, username, password);
                for(Account databaseAccount : databaseAccounts) {
                    // Same username and hashed password
                    if(databaseAccount.equals(tempAccount)) {
                        return new ResponseEntity<Account>(databaseAccount, HttpStatus.OK);
                    }
                }
                // Account exists in the system, but wrong login information was provided by user.
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            // Account does not exist in system, need to tell user to create one
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        // Something went wrong not related to user authentication.
        } catch(IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
