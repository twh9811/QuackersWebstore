package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.model.UserAccount;
import com.ducks.api.ducksapi.persistence.AccountDAO;

/**
 * 
 * Handles the REST API requests for the Account resources
 * 
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Travis Hill
 */

@RestController
@RequestMapping("/")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private AccountDAO accountDAO;

    /**
     * Creates a REST API controller to reponds to requests specifically for
     * accoount data
     * 
     * @param accountDAO The {@link AccountDAO Account Data Access Object} to
     *                   perform CRUD operations
     * 
     *                   This dependency is injected by the Spring Framework
     */
    public UserController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Responds to the POST request for a {@linkplain Account account} being created
     * 
     * @param account The {@link Account account} to create
     * @return ResponseEntity with created {@link Account account} object and HTTP
     *         status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Account
     *         account} object already exists<br>
     *         ResponseEntity with HTTP status of NOT_ACCEPTABLE if {@link Account}
     *         has weak password
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> createUser(@RequestBody Account account) {
        // curl.exe -X POST -H 'Content-Type:application/json'
        // 'http://localhost:8080/accounts' -d '{\"type\":\"UserAccount\",
        // \"id\":1,\"username\":\"TEST\",\"plainPassword\":\"TEST\"}'
        LOG.log(Level.INFO, "POST /accounts {0}", account);
        try {
            Account newAccount = accountDAO.createAccount(account);
            // Username doesn't already exist in system, OK to create account
            if (newAccount != null) {
                if (newAccount.validateStrongPassword(account.getPlainPassword())) {
                    return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }

            }
            // Account exists in the system already.
            return new ResponseEntity<>(HttpStatus.CONFLICT);
            // Something went wrong not related to user creation.
        } catch (IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * Responds to the GET request for a {@linkplain Account account} attempting to
     * login to the site
     * We want to get the database account to load all their data.
     * 
     * @param username The username of the login attempt
     * @param password The password of the login attempt
     * @return The account in the database if attempt successful with all saved data
     * 
     *         HttpStatus OK if the authentication was successful
     *         HttpStatus CONFLICT if the authentication failed
     *         HttpStatus NOT_FOUND if the login attempt was for an account was not
     *         found in the database
     *         HttpStatus INTERNAL_SERVER_ERROR otherwise.
     */
    @GetMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestParam String username, @RequestParam String password) {
        // curl.exe -X GET 'http://localhost:8080/login?username=TEST&password=TEST'
        LOG.log(Level.INFO, "GET /login {0}", username + " " + password);

        try {
            Account[] databaseAccounts = accountDAO.findAccounts(username);
            // This means account does exist in system
            if (databaseAccounts.length != 0) {
                // Create a temporary account. Only one account with the username can exist so
                // ID doesn't matter
                UserAccount tempAccount = new UserAccount(-100, username, password);
                for (Account databaseAccount : databaseAccounts) {
                    // Same username and hashed password
                    if (databaseAccount.equals(tempAccount)) {
                        return new ResponseEntity<>(databaseAccount, HttpStatus.OK);
                    }
                }
                // Account exists in the system, but wrong login information was provided by
                // user.
                return new ResponseEntity<>(HttpStatus.CONFLICT);
                // Account does not exist in system, need to tell user to create one
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Something went wrong not related to user authentication.
        } catch (IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the POST request for a {@linkplain Account account} attempting to
     * logout of the site
     * Saves current changes to account
     * 
     * @param account The account to be updated.
     * @return
     *         HttpStatus OK if the account was saved successfully
     *         HttpStatus CONFLICT if the account failed to save
     *         HttpStatus INTERNAL_SERVER_ERROR otherwise.
     */
    @PutMapping("/logout")
    public ResponseEntity<Account> logoutUser(@RequestBody Account account) {
        // curl.exe -X PUT -H 'Content-Type:application/json'
        // 'http://localhost:8080/logout' -d '{\"type\":\"UserAccount\",
        // \"id\":1,\"username\":\"TEST\",\"plainPassword\":\"TEST1#sdf7csdf\"}'
        LOG.log(Level.INFO, "PUT /logout {0}", account);
        try {
            Account updatedAccount = accountDAO.updateAccount(account);
            // Account saved successfully
            if (updatedAccount != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            // Account did not save. Most likely an empty account body was entered (not
            // logged in)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException ioe) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Account account}
     * 
     * @param id The id used to locate the {@link Account account}
     * @return The account in the database
     * 
     *         HttpStatus OK if account was found
     *         HttpStatus NOT_FOUND if the account was not found
     *         HttpStatus INTERNAL_SERVER_ERROR otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        // curl.exe -X GET 'http://localhost:8080/TESTID
        LOG.log(Level.INFO, "GET /{0}", id);

        try {
            Account account = accountDAO.getAccount(id);
            if (account != null) {
                return new ResponseEntity<>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Account account}
     * 
     * @return ResponseEntity with array of {@link Account account} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Account[]> getAccounts() {
        // curl.exe -X GET 'http://localhost:8080/
        LOG.log(Level.INFO, "GET /");

        try {
            Account[] accounts = accountDAO.getAccounts();
            if (accounts.length != 0) {
                return new ResponseEntity<>(accounts, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for updating an account
     * 
     * @param account The account being updated
     * @return 404 if account doesn't exist
     *         406 if password isn't strong
     *         200 + account obj if updated successfully
     *         500 if dao fails
     */
    @PutMapping("/updateaccount")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        LOG.log(Level.INFO, "PUT /updateaccount {0}", account);
        try {
            // Check if account exists
            if (accountDAO.getAccount(account.getId()) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update account
            Account updated = accountDAO.updateAccount(account);

            // Check if account updated
            if (updated == null) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            return new ResponseEntity<Account>(updated, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Account account} with the given id
     * 
     * @param id The id of the {@link Account account} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable int id) {
        // curl.exe -X DELETE 'http://localhost:8080/TESTID
        LOG.log(Level.INFO, "DELETE /{0}", id);
        try {
            boolean accountDeleted = accountDAO.deleteAccount(id);
            if (accountDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
