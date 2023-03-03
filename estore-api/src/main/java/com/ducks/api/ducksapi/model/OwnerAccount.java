package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

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
        super();
    }
}
