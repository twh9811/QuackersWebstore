package com.ducks.api.ducksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/** 
 * Represents a Account entity. Used to store User information for the Webstore.
 * 
 * @author Travis Hill
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserAccount.class, name = "UserAccount"),
    @JsonSubTypes.Type(value = OwnerAccount.class, name = "OwnerAccount")
})
public abstract class Account {

        @JsonProperty("id")
        private int id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("plainPassword")
        private String plainPassword;

        @JsonProperty("adminStatus")
        private boolean adminStatus;

    /**
     * Needed for Spring to run the server. Needs Public Default Constructor.
     */
    public Account() {}

    // Used for creating new Account objects in the DAO.
    public Account(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword, @JsonProperty("adminStatus") boolean adminStatus) {
        this.id = id;
        this.username = username;
        this.plainPassword = plainPassword;
        this.adminStatus = adminStatus;
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
     * @return the password of the account
     */
    public String getPassword() {
        return plainPassword;
    }

    /**
     * Changes the password of an account
     * @param newPassword The passworld the account should change to.
     */
    public void setPassword(String newPassword) {
        this.plainPassword = newPassword;
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
        if(this.username.equals("admin")) {
            this.adminStatus = statusType;
        }
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public String toString() {
        return username + ":" + plainPassword;
    }

    /**
     * ID and admin status don't matter in this regard. This is solely for login verification purposes.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Account) {
            Account other = (Account) obj;
            return (this.username.equals(other.getUsername()) && this.plainPassword == other.getPassword());
        } 
        return false;
    }
}
