package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Account class that represents an regular user on the webstore.
 * 
 * @author Travis Hill
 */
@Component
public class UserAccount extends Account {

    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("plainPassword")
    private String plainPassword;

    @JsonProperty("hashedPassword")
    private int hashedPassword;

    public UserAccount() {
        super();
    }
    
    /**
     * Creates a new regular user account using the constructor defined in the Account class.
     * Specifically for initially creating the account.
     * @param id the account ID
     * @param username the account username
     * @param plainPassword the account password in plaintext
     */
    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword) {
        super(id, username, plainPassword);
    }

    /**
     * Creates a new regular user account using the constructor defined in the Account class.
     * Specifically for updating pre-existing accounts
     * @param id the account ID
     * @param username the account username
     * @param hashedPassword the account password but hashed
     */
    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("hashedPassword") int hashedPassword) {
        super(id, username, hashedPassword);
    }
}
