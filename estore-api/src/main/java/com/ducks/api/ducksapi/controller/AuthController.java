package com.ducks.api.ducksapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
