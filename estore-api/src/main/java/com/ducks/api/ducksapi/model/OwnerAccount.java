package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A Account class that represents an Admin on the webstore.
 * 
 * @author Travis Hill
 */
@JsonTypeName("admin")
@Component
public class OwnerAccount extends Account {

    /**
     * Creates a new Admin account using the default values defined in the Account class.
     */
    public OwnerAccount() {
        super(0, "admin", "admin", true);
    }
}
