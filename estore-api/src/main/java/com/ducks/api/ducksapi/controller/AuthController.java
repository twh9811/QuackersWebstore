package com.ducks.api.ducksapi.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.persistence.AccountDAO;

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


    /**
     * 
     * Responds to the POST request for a {@linkplain Account account} attempting to login to the site
     * 
     * @param account A Login attempt by the user (not linked to the database)
     * @return The account in the database if attempt successful
     * 
     * HttpStatus OK if the authentication was successful
     * HttpStatus CONFLICT if the authentication failed
     * HttpStatus NOT_FOUND if the login attempt was for an account was not found in the database
     */
    @PostMapping("/login")
    public ResponseEntity<Account> authenticateUser(@PathVariable Account account) {
        try {
            int accountToLookFor = account.getId();
            Account databaseAccount = accountDAO.getAccount(accountToLookFor);
            // This means account DOES exist in system
            if(databaseAccount != null) {
                // If the accounts match, successful login
                if(databaseAccount.equals(account)) {
                    return new ResponseEntity<Account>(databaseAccount, HttpStatus.OK);
                // Accounts don't match, unsuccessful login
                } else {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
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
