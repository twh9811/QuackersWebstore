package com.ducks.api.ducksapi.persistence;

import java.io.IOException;

import com.ducks.api.ducksapi.model.Duck;

/**
 * Defines the interface for duck object persistence
 * 
 * @author SWEN Faculty
 */
public interface DuckDAO {
    /**
     * Retrieves all {@linkplain Duck ducks}
     * 
     * @return An array of {@link Duck duck} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Duck[] getDucks() throws IOException;

    /**
     * Finds all {@linkplain Duck ducks} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Duck ducks} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Duck[] findDucks(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Duck duck} with the given id
     * 
     * @param id The id of the {@link Duck duck} to get
     * 
     * @return a {@link Duck duck} object with the matching id
     * <br>
     * null if no {@link Duck duck} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Duck getDuck(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Duck duck}
     * 
     * @param duck {@linkplain Duck duck} object to be created and saved
     * <br>
     * The id of the duck object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Duck duck} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Duck createDuck(Duck duck) throws IOException;

    /**
     * Updates and saves a {@linkplain Duck duck}
     * 
     * @param {@link Duck duck} object to be updated and saved
     * 
     * @return updated {@link Duck duck} if successful, null if
     * {@link Duck duck} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Duck updateDuck(Duck duck) throws IOException;

    /**
     * Deletes a {@linkplain Duck duck} with the given id
     * 
     * @param id The id of the {@link Duck duck}
     * 
     * @return true if the {@link Duck duck} was deleted
     * <br>
     * false if duck with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteDuck(int id) throws IOException;
}
