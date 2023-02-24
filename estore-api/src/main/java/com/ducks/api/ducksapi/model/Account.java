package com.ducks.api.ducksapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Account(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = plainPassword.hashCode();
        this.adminStatus = false;
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
        this.adminStatus = statusType;
    }

    public boolean changePassword(String originalPass, String newPass) {
        int checkHash = originalPass.hashCode();
        if(checkHash == getHashedPassword()) {
            int newhash = newPass.hashCode();
            setHashedPassword(newhash);
            return true;
        }
        return false;
    }

}
