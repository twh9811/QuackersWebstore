package com.ducks.api.ducksapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {

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

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(int hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(boolean statusType) {
        // If you aren't an admin, you can't change account status.
        if(this.adminStatus) {
            this.adminStatus = statusType;
        }
    }

    @Override
    public String toString() {
        return username + ":" + hashedPassword;
    }

}
