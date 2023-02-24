package com.ducks.api.ducksapi.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.ducks.api.ducksapi.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * Implements the functionality for JSON file-based persistance for Accounts
 * 
 * @author Travis Hill
 */

public class AccountFileDAO implements AccountDAO{

    Map<Integer, Account> accounts; // Creates a local cache of account objects so the file doesn't have to
                                    // be read from each time
    private ObjectMapper objectMapper; // Provides conversion between Account objects and JSON text format
                                       // written to the file
    private static int nextID; // The next ID to assign to a account
    private String filename; //Filename to read and write to
    

    /**
     * Creates a Account File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object Serialization/Deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AccountFileDAO(String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
    }

    /**
     * 
     * @return true if the {@link Account accounts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        // TODO
        return true;
    }

    private boolean load() throws IOException {
        // TODO
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
    @Override
    public Account[] getAccounts() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccounts'");
    }

    @Override
    public Account[] findAccounts(String containsText) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAccounts'");
    }

    @Override
    public Account getAccount(int id) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccount'");
    }

    @Override
    public Account createAccount(Account account) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public Account updateAccount(Account Account) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }

    @Override
    public boolean deleteAccount(int id) throws IOException {
        // Handles multiple clickEvents
        synchronized(accounts) {
            // Checks if account is in database
            if(accounts.containsKey(id)) {
                accounts.remove(id);
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
                int checkHash = originalPass.hashCode();
                int currentHash = account.getHashedPassword();
                // checks if they have permission to change password
                if(checkHash == currentHash) {
                    int newhash = newPass.hashCode();
                    //changes password
                    account.setHashedPassword(newhash);
                    return true;
                }
            }
            // Account not in database, can't change password
            return false;
        }
        
    }
    
}
