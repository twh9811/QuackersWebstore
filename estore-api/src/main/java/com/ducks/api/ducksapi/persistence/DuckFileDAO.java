package com.ducks.api.ducksapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ducks.api.ducksapi.model.Duck;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Ducks
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class DuckFileDAO implements DuckDAO {
    private static final Logger LOG = Logger.getLogger(DuckFileDAO.class.getName());
    Map<Integer,Duck> ducks;   // Provides a local cache of the duck objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Duck
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new duck
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Duck File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public DuckFileDAO(@Value("${ducks.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the ducks from the file
    }

    /**
     * Generates the next id for a new {@linkplain Duck duck}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Duck ducks} from the tree map
     * 
     * @return  The array of {@link Duck ducks}, may be empty
     */
    private Duck[] getDucksArray() {
        return getDucksArray(null);
    }

    /**
     * Generates an array of {@linkplain Duck ducks} from the tree map for any
     * {@linkplain Duck ducks} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Duck ducks}
     * in the tree map
     * 
     * @return  The array of {@link Duck ducks}, may be empty
     */
    private Duck[] getDucksArray(String containsText) { // if containsText == null, no filter
        ArrayList<Duck> duckArrayList = new ArrayList<>();

        for (Duck duck : ducks.values()) {
            if (containsText == null || duck.getName().contains(containsText)) {
                duckArrayList.add(duck);
            }
        }

        Duck[] duckArray = new Duck[duckArrayList.size()];
        duckArrayList.toArray(duckArray);
        return duckArray;
    }

    /**
     * Saves the {@linkplain Duck ducks} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Duck ducks} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Duck[] duckArray = getDucksArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),duckArray);
        return true;
    }

    /**
     * Loads {@linkplain Duck ducks} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        ducks = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of ducks
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Duck[] duckArray = objectMapper.readValue(new File(filename),Duck[].class);

        // Add each duck to the tree map and keep track of the greatest id
        for (Duck duck : duckArray) {
            ducks.put(duck.getId(),duck);
            if (duck.getId() > nextId)
                nextId = duck.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Duck[] getDucks() {
        synchronized(ducks) {
            return getDucksArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Duck[] findDucks(String containsText) {
        synchronized(ducks) {
            return getDucksArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Duck getDuck(int id) {
        synchronized(ducks) {
            if (ducks.containsKey(id))
                return ducks.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Duck createDuck(Duck duck) throws IOException {
        synchronized(ducks) {
            // We create a new duck object because the id field is immutable
            // and we need to assign the next unique id
            // Modified the constructor to handle duck accessories # Travis 2/15
            Duck newDuck = new Duck(nextId(),duck.getName(),duck.getSize(),duck.getColor(),duck.getHatUID(),duck.getShirtUID(),duck.getShoesUID(),duck.getHandItemUID(),duck.getJewelryUID());
            ducks.put(newDuck.getId(),newDuck);
            save(); // may throw an IOException
            return newDuck;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Duck updateDuck(Duck duck) throws IOException {
        synchronized(ducks) {
            if (ducks.containsKey(duck.getId()) == false)
                return null;  // duck does not exist

            ducks.put(duck.getId(),duck);
            save(); // may throw an IOException
            return duck;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteDuck(int id) throws IOException {
        synchronized(ducks) {
            if (ducks.containsKey(id)) {
                ducks.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
