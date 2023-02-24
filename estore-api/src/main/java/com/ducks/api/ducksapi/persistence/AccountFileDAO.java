package com.ducks.api.ducksapi.persistence;

import java.io.IOException;
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

    private boolean save() throws IOException {
        // TODO
        return true;
    }

    private boolean load() throws IOException {
        return true;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }

    @Override
    public boolean changePassword(int id, String originalPass, String newPass) throws IOException{
        Account account = getAccount(id);
        int checkHash = originalPass.hashCode();
        int currentHash = account.getHashedPassword();
        if(checkHash == currentHash) {
            int newhash = newPass.hashCode();
            account.setHashedPassword(newhash);
            return true;
        }
        return false;
    }
    
}
