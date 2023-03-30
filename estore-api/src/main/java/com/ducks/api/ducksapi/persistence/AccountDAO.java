package com.ducks.api.ducksapi.persistence;

import java.io.IOException;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.model.UserAccount;

/**
 * Defines the interface for account object persistence
 * 
 * @author Travis Hill
 */
public interface AccountDAO {

    /**
     * Retrieves all {@linkplain Account accounts}
     * 
     * @return An array of {@link Account account} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */

    Account[] getAccounts() throws IOException;

    /**
     * Finds all {@linkplain Account accounts} whose username contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Account accounts} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Account[] findAccounts(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Account account} with the given id
     * 
     * @param id The id of the {@link Account account} to get
     * 
     * @return a {@link Account account} object with the matching id
     * <br>
     * null if no {@link Account account} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Account getAccount(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Account account}
     * 
     * @param account {@linkplain Account account} object to be created and saved
     * <br>
     * The id of the account object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Account account} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Account createAccount(Account account) throws IOException;

    /**
     * Updates and saves a {@linkplain Account account}
     * 
     * @param {@link Account account} object to be updated and saved
     * 
     * @return updated {@link Account account} if successful, null if
     * {@link Account account} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Account updateAccount(Account account) throws IOException;

    /**
     * Deletes a {@linkplain Account account} with the given id
     * 
     * @param id The id of the {@link Account account}
     * 
     * @return true if the {@link Account account} was deleted
     * <br>
     * false if account with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteAccount(int id) throws IOException;

    /**
     * Changes the original password of a {@linkplain Account account} with the new Password.
     * 
     * @param id The id of the {@link Account account} that needs their password changed
     * 
     * @param originalPass The original password of the {@link Account account}
     * 
     * @param newPass The new password of the {@link Account account}
     * 
     * @return true if the password of the {@link Account account} was changed
     * <br>
     * false if account password was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changePassword(int id, String originalPass, String newPass) throws IOException;


    /**
     * Changes the First Name of a {@linkplain Account account} with the new First Name.
     * 
     * @param id The id of the {@link Account account} that needs their First Name changed
     * 
     * @param newName The new First Name of the {@link Account account}
     * 
     * @return true if the First Name of the {@link Account account} was changed
     * <br>
     * false if account First name was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changeFirstName(int id, String newName) throws IOException;

    /**
     * Changes the Last Name of a {@linkplain Account account} with the new Last Name.
     * 
     * @param id The id of the {@link Account account} that needs their Last Name changed
     * 
     * @param newName The new Last Name of the {@link Account account}
     * 
     * @return true if the Last Name of the {@link Account account} was changed
     * <br>
     * false if account Last name was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changeLastName(int id, String newName) throws IOException;

    /**
     * Changes the Address of a {@linkplain Account account} with the new Address.
     * 
     * @param id The id of the {@link Account account} that needs their Address changed
     * 
     * @param newAddress The new Address of the {@link Account account}
     * 
     * @return true if the Address of the {@link Account account} was changed
     * <br>
     * false if account Address was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changeAddress(int id, String newAddress) throws IOException;

    /**
     * Changes the City of a {@linkplain Account account} with the new City.
     * 
     * @param id The id of the {@link Account account} that needs their City changed
     * 
     * @param newCity The new City of the {@link Account account}
     * 
     * @return true if the City of the {@link Account account} was changed
     * <br>
     * false if account City was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changeCity(int id, String newCity) throws IOException;

    /**
     * Changes the ZipCode of a {@linkplain Account account} with the new ZipCode.
     * 
     * @param id The id of the {@link Account account} that needs their ZipCode changed
     * 
     * @param newZipCode The new ZipCode of the {@link Account account}
     * 
     * @return true if the ZipCode of the {@link Account account} was changed
     * <br>
     * false if account ZipCode was not changed
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean changeZipCode(int id, String newZipCode) throws IOException;

}
