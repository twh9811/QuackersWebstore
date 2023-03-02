package com.ducks.api.ducksapi.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("accounts")
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
     * Responds to the GET request for a ({@linkplain} Account account} for the specific ID
     * This is needed to get valid data to compare to the potentially invalid user-input data
     * 
     * @param id the id of the account to be fetched
     * @return the account object
     * 
     * ResponseEntity with HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of Internal Server Error if anything else happens
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        try {
            Account account = accountDAO.getAccount(id);
            if(account != null) {
                return new ResponseEntity<Account>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
