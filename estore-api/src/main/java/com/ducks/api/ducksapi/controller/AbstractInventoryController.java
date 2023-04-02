package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.persistence.DuckDAO;

public abstract class AbstractInventoryController {
    protected DuckDAO duckDao;
    private Logger log;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param duckDao The {@link DuckDAO Duck Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    protected AbstractInventoryController(DuckDAO duckDao, Logger log) {
        this.duckDao = duckDao;
        this.log = log;
    }

    /**
     * Responds to the GET request for a {@linkplain Duck duck} for the given id
     * 
     * @param id The id used to locate the {@link Duck duck}
     * 
     * @return ResponseEntity with {@link Duck duck} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    public ResponseEntity<Duck> getDuck(int id, String logMessage) {
        log.log(Level.INFO, logMessage, id);
        try {
            Duck duck = duckDao.getDuck(id);
            if (duck != null) {
                return new ResponseEntity<>(duck, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Duck ducks}
     * 
     * @return ResponseEntity with array of {@link Duck ducks} objects (may be
     *         empty) and HTTP status of OK<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if no ducks are
     *         found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    public ResponseEntity<Duck[]> getDucks(String logMessage) {
        log.info(logMessage);
        try {
            Duck[] ducks = duckDao.getDucks();
            if (ducks != null && ducks.length != 0) {
                return new ResponseEntity<>(ducks, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException ioe) {
            log.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Duck ducks} whose name
     * contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the
     *             {@link Duck ducks}
     * 
     * @return ResponseEntity with array of {@link Duck duck} objects (may be empty)
     *         and HTTP status of OK<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if no ducks are
     *         found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         Example: Find all ducks that contain the text "ma"
     *         GET http://localhost:8080/inventory/?name=ma
     */
    public ResponseEntity<Duck[]> searchDucks(String name, String logMessage) {
        log.log(Level.INFO, logMessage, name);
        try {
            Duck[] ducks = duckDao.findDucks(name);

            if (ducks != null && ducks.length != 0) {
                return new ResponseEntity<>(ducks, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (IOException ioe) {
            log.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Duck duck} with the provided duck object
     * 
     * @param duck - The {@link Duck duck} to create
     * 
     * @return ResponseEntity with created {@link Duck duck} object and HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Duck duck}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         Will inherently throw a 400 if the duck is invalid
     */
    public ResponseEntity<Duck> createDuck(Duck duck, String logMessage) {
        log.log(Level.INFO, logMessage, duck);
        try {
            Duck newDuck = duckDao.createDuck(duck);
            if (newDuck != null) {
                return new ResponseEntity<>(newDuck, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException ioe) {
            log.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Duck duck} with the provided {@linkplain Duck duck}
     * object, if it exists
     * 
     * @param duck The {@link Duck duck} to update
     * 
     * @return ResponseEntity with updated {@link Duck duck} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    public ResponseEntity<Duck> updateDuck(Duck duck, String logMessage) {
        log.log(Level.INFO, logMessage, duck);
        try {
            // Makes sure that a duck with this name & different id does not already exist
            Duck foundDuck = duckDao.getDuckByName(duck.getName());
            if (foundDuck != null && foundDuck.getId() != duck.getId()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Duck updateDuck = duckDao.updateDuck(duck);
            if (updateDuck != null) {
                return new ResponseEntity<>(updateDuck, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException ioe) {
            log.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Duck duck} with the given id
     * 
     * @param id The id of the {@link Duck duck} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    public ResponseEntity<Duck> deleteDuck(int id, String logMessage) {
        log.log(Level.INFO, logMessage, id);
        try {
            if (duckDao.deleteDuck(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException ioe) {
            log.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
