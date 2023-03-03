package com.ducks.api.ducksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/** 
 * Represents a Account entity. Used to store User information for the Webstore.
 * 
 * @author Travis Hill
 */

public abstract class Account {

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

    // Initial account creation, never want to store plain password anywhere. Only able to be yoinked in transit
    public Account(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = plainPassword.hashCode();
        this.adminStatus = false;
    }

    // Used for creating new Account objects in the DAO. Don't want to have a getter for plaintext password.
    public Account(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("hashedPassword") int hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.adminStatus = false;
    }

     // Creates an admin account.
     public Account() {
        this.id = 0;
        this.username = "admin";
        this.hashedPassword = "admin".hashCode();
        this.adminStatus = true;
    }

    /**
     * @return the id of the account
     */
    public int getId() {
        return id;
    }

    /**
     * @return the username of the account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the username of the account
     * @param username The username the account should be renamed to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the hashed password of the account
     */
    public int getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Changes the hashed password of an account
     * @param hashedPassword The hashed passworld the account should change to.
     */
    public void setHashedPassword(int hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return Boolean stating whether the account is admin (true) or not (false)
     */
    public boolean getAdminStatus() {
        return adminStatus;
    }
    
    /**
     * Changes the admin status of an account
     * @param statusType The type of status the account should be changed to
     */
    public void setAdminStatus(boolean statusType) {
        // If you aren't an admin, you can't change account status.
        if(this.adminStatus) {
            this.adminStatus = statusType;
        }
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public String toString() {
        return username + ":" + hashedPassword;
    }

    /**
     * ID and admin status don't matter in this regard. This is solely for login verification purposes.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Account) {
            Account other = (Account) obj;
            return (this.username.equals(other.getUsername()) && this.hashedPassword == other.getHashedPassword());
        } 
        return false;
    }
}
