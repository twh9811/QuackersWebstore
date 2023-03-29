package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A Account class that represents an regular user on the webstore.
 * 
 * @author Travis Hill
 */
@JsonTypeName("user")
@Component
public class UserAccount extends Account {

    // information created during regestration
    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("plainPassword")
    private String plainPassword;

    // information created during shipping details
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("zipCode")
     private String zipCode;

    /**
     * Needed for Spring to run the server. Needs Public Default Constructor.
     */
    public UserAccount() {}

    /**
     * Creates a new regular user account using the constructor defined in the Account class.
     * @param id the account ID
     * @param username the account username
     * @param plainPassword the account password in plaintext
     */
    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("plainPassword") String plainPassword) {
        super(id, username, plainPassword, false);
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.city = "";
        this.zipCode = "";
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
    
}
