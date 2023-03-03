package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Account class that represents an Admin on the webstore.
 * 
 * @author Travis Hill
 */
@Component
public class OwnerAccount extends Account {

    /**
     * Creates a new Admin account using the default values defined in the Account class.
    */
    public OwnerAccount() {
        super(0, "admin", "admin");
        super.setAdminStatus(true);
    }
}
