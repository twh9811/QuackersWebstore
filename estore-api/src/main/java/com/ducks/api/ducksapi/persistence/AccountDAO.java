package com.ducks.api.ducksapi.persistence;

import java.io.IOException;

import com.ducks.api.ducksapi.model.Account;

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

}
