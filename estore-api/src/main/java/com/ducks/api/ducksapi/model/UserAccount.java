package com.ducks.api.ducksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccount extends Account {

    @JsonProperty("id")
        private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("plainPassword")
    private String plainPassword;

    @JsonProperty("hashedPassword")
    private int hashedPassword;

    @JsonProperty("adminStatus")
    private boolean adminStatus;
    
    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword) {
        super(id, username, plainPassword);
    }

    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("hashedPassword") int hashedPassword) {
        super(id, username, hashedPassword);
    }
}
