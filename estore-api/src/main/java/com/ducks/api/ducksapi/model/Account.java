package com.ducks.api.ducksapi.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // information created during shipping details
        @JsonProperty("firstName")
        private String firstName;

        @JsonProperty("lastName")
        private String lastName;

        @JsonProperty("address")
        private String address;

        @JsonProperty("city")
        private String city;

        @JsonProperty("zipCode")
        private String zipCode;

        Pattern regex = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");

    /**
     * Needed for Spring to run the server. Needs Public Default Constructor.
     */
    public Account() {}

    // Used for creating new Account objects in the DAO.
    public Account(@JsonProperty("id") int id, @JsonProperty("username") String username,
                   @JsonProperty("plainPassword") String plainPassword, @JsonProperty("adminStatus") boolean adminStatus,
                   @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, 
                   @JsonProperty("address") String address, @JsonProperty("city") String city,
                   @JsonProperty("zipCode") String zipCode) {
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
    public String getPlainPassword() {
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
     * Checks if the entered password matches the current password
     * @param newPassword The input that is being checked
     * @return whether or not the passwords match
     */
    public boolean confirmPassword(String original, String current){
        return original.equals(current);
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
     * Takes password associated with the account and determines if it is strong enough or not.
     * Must contain 8 characters, 1 lowercase, 1 uppercase and a number. It CAN contain special chars, not required.
     *
     * @return True if the password meets requirements, false otherwise.
     */
    public boolean validateStrongPassword(String password) {
        Matcher matcher = regex.matcher(password);
        return matcher.find();
    }

    /**
     * @return the First Name of the account
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Changes the First Name of the account
     * @param firstName The First Name the account should be renamed to
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the Last Name of the account
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Changes the Last Name of the account
     * @param lastName The Last Name the account should be renamed to
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the Address of the account
     */
    public String getAddress() {
        return address;
    }

    /**
     * Changes the Address of the account
     * @param address The Address the account should be renamed to
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the City of the account
     */
    public String getCity() {
        return city;
    }

    /**
     * Changes the City of the account
     * @param city The City the account should be renamed to
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the ZipCode of the account
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Changes the ZipCode of the account
     * @param zipCode The ZipCode the account should be renamed to
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
            return (this.username.equals(other.getUsername()) && this.plainPassword.equals(other.getPlainPassword()));
        } 
        return false;
    }
}
