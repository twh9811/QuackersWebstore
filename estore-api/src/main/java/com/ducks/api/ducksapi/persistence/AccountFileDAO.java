package com.ducks.api.ducksapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ducks.api.ducksapi.model.Account;
import com.ducks.api.ducksapi.model.OwnerAccount;
import com.ducks.api.ducksapi.model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * Implements the functionality for JSON file-based persistance for Accounts
 * 
 * @author Travis Hill
 */

@Component
public class AccountFileDAO implements AccountDAO{

    Map<Integer, Account> accounts; // Creates a local cache of account objects so the file doesn't have to
                                    // be read from each time
    private ObjectMapper objectMapper; // Provides conversion between Account objects and JSON text format
                                       // written to the file
    private static int nextID; // The next ID to assign to a account
    private String filename; //Filename to read and write to
    private Account adminAccount = new OwnerAccount(); // Reserved owner account.

    /**
     * Creates a Account File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object Serialization/Deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AccountFileDAO(@Value("${accounts.file}")String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
        createAccount(adminAccount);
    }

    /**
     * 
     * @return true if the {@link Account accounts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Account[] accountArray = getAccountsArray();

        // Serializes the Java Objects to JSON objects into the file
        // Throws IOException if an error occurs reading/writing to the file
        objectMapper.writeValue(new File(filename), accountArray);

        return true;
    }
    
    /**
     * Loads {@linkplain Account accounts} from the JSON file and stores them into a Map.
     * <br>
     * Sets the next ID to be greatest id found in the file.
     * 
     * @return true if the file was loaded/read successfully
     * 
     * @throws IOException when file cannot be read or accessed.
     */
    private boolean load() throws IOException {
        accounts = new TreeMap<>();
        nextID = 0;

        // Deserializes the JSON Objects in the file to an array of accounts.
        // Throws IOException if an error occurs reading/accessing the file
        Account[] accountArray = objectMapper.readValue(new File(filename), Account[].class);

        // Add each account to the tree map and find the greatest ID.
        for(Account account : accountArray) {
            // Uses account ID as key in map, stores account as value to key.
            accounts.put(account.getId(), account);
            int currAccountID = account.getId();
            if(currAccountID > nextID) {
                nextID = currAccountID;
            }
        }
        // Now we have the greatest account ID, so increment it by one to prevent duplicates
        // use pre-increment since its stored in a variable.
        ++nextID;
        return true;
    }

    /**
     * Generates an array of {@linkplain Account accounts} from the tree map
     * 
     * @return The array of {@link Account accounts}, may be empty.
     */
    private Account[] getAccountsArray() {
        return getAccountsArray(null);
    }

    /**
     * Generates an array of {@linkplain Account accounts} from the tree map, specifically
     * any {linkplain Account accounts} that contain the text specified by contains.
     * <br>
     * If contains is null, then all {@linkplain Account accounts} should be returned since there is no filter.
     * 
     * @return The array of {@link Account accounts}, it can be empty.
     */
    private Account[] getAccountsArray(String contains) {
        ArrayList<Account> accountArrayList = new ArrayList<>();

        // Loops through all accounts in tree map
        for(Account account : accounts.values()) {
            // If the account matches the filter, add it to the array list.
            if(contains == null || account.getUsername().contains(contains)) {
                accountArrayList.add(account);
            }
        }

        // Convert the array list to a regular array, makes it static/fixrd size and hard to modify by accident
        Account[] accountArray = new Account[accountArrayList.size()];
        accountArrayList.toArray(accountArray);
        return accountArray;
    }

    /**
     * Generates the next id for a new {@linkplain} Account account}
     * 
     * @return The next unique id
     */
    private synchronized static int nextID() {
        int id = nextID;
        ++nextID;
        return id;
    }

    /**
     * * {@inheritDoc}}
     */
    @Override
    public Account[] getAccounts() throws IOException {
        // Handles multiple click events
        synchronized(accounts) {
            // No filter
            return getAccountsArray();
        }
    }

    /**
    * * {@inheritDoc}}
    */
    @Override
    public Account[] findAccounts(String containsText) throws IOException {
        // Handles multiple click events
        synchronized(accounts) {
            // With filter
            return getAccountsArray(containsText);
        }
    }

    /**
    * * {@inheritDoc}}
    */
    @Override
    public Account getAccount(int id) throws IOException {
        // Handles multiple click events
        synchronized(accounts) {
            // If accounts has account id return it.
            if(accounts.containsKey(id)) {
                return accounts.get(id);
            }
            // If the ID doesn't exist, return null
            return null;
        }
    }

    /**
    * * {@inheritDoc}}
    */
    @Override
    public Account createAccount(Account account) throws IOException {
       // Handles multiple click events
       synchronized(accounts) {
            // Create new account object with nextID as its unique ID.
            // First we check if the account username already exists
            Account[] accountArray = getAccountsArray(account.getUsername());
            for(Account existingAccount : accountArray) {
                String existingUsername = existingAccount.getUsername();
                // Username already exists in the inventory, return null
                if(existingUsername.equalsIgnoreCase(account.getUsername())) {
                    return null;
                }
            }
            Account newAccount;
            // This account should be the only admin account made by the FileDAO. The rest should be made via the pre-made admin account and setAdminStatus.
            if(account.getUsername().equals("admin")) {
                newAccount = new OwnerAccount();
            // Create regular user account
            } else {
                newAccount = new UserAccount(nextID(), account.getUsername(), account.getPlainPassword());
            }
            
            // Don't save account to database if the password is invalid.
            // However we want the account object still to use in the UserController for feedback purposes.
            if(account.validateStrongPassword(account.getPlainPassword())) {
                accounts.put(newAccount.getId(), newAccount);
                // Save changes to the database
                save();
            }
            return newAccount;
       }
    }

    /**
    * * {@inheritDoc}}
    */
    @Override
    public Account updateAccount(Account account) throws IOException {
        // Handles multiple click events
        synchronized(accounts) {
            int accountID = account.getId();
            // If the database has the account in it, put the changed account into it.
            if(accounts.containsKey(accountID)) {
                accounts.put(accountID, account);
                // Save changes to database.
                save();
                return account;
            }
            return null;
        }
    }

    @Override
    public boolean deleteAccount(int id) throws IOException {
        // Handles multiple clickEvents
        synchronized(accounts) {
            // Checks if account is in database
            if(accounts.containsKey(id)) {
                accounts.remove(id);
                return true;
            }
            // Account is not in database, therefore cannot be deleted
            return false;
        }
    }

    @Override
    public boolean changePassword(int id, String originalPass, String newPass) throws IOException{
        // Handles multiple clickEvents
        synchronized(accounts) {
            // Checks if account is in database
            if(accounts.containsKey(id)) {
                Account account = getAccount(id);
                String currPassword = account.getPlainPassword();
                // checks if they have permission to change password
                if(originalPass.equals(currPassword)) {
                    // checks if the newPassword is a strong password
                    if(account.validateStrongPassword(newPass)){
                        //changes password
                        account.setPassword(newPass);
                        // Save changes to database
                        return save();
                    }
                }
            }
            // Account not in database, can't change password
            return false;
        }
    }
}
