package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.DuckOutfit;
import com.ducks.api.ducksapi.persistence.DuckDAO;

/**
 * Handles the REST API requests for the Duck resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Beining Zhou
 */

@RestController
@RequestMapping("outfit")
public class CustomizeController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private DuckDAO duckDao;
     
    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param duckDao The {@link DuckDAO Duck Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public CustomizeController(@Qualifier("customDuckFileDAO") DuckDAO duckDao){
        this.duckDao = duckDao;
    }
    
    /**
     * Responds to the GET request for the {@linkplain DuckOutfit duckOutfit} for the duck with given id
     * 
     * @param id The id used to locate the {@link Duck duck}
     * 
     * @return ResponseEntity with {@linkplain DuckOutfit duckOutfit} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<DuckOutfit> getDuckOutfit(@PathVariable int id) {
        LOG.info("GET /outfit/" + id);
        try {
            Duck duck = duckDao.getDuck(id);
            if (duck != null) {
                DuckOutfit duckOutfit = duck.getOutfit();
                if (duckOutfit != null) {
                    return new ResponseEntity<DuckOutfit>(duckOutfit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sets the outfit {@linkplain DuckOutfit duckOutfit} of the duck with the provided id,
     * if it exists
     * 
     * @param duckOutfit The {@linkplain DuckOutfit duckOutfit} to set
     * 
     * @return ResponseEntity with updated {@linkplain DuckOutfit duckOutfit} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{id}")
    public ResponseEntity<DuckOutfit> setDuckOutfit(@RequestBody DuckOutfit duckOutfit, @PathVariable int id) {
        LOG.info("POST /outfit/{id}/ " + duckOutfit);
        try {
            duckDao.getDuck(id).setOutfit(duckOutfit);
            DuckOutfit newDuckOutfit = duckDao.getDuck(id).getOutfit();
            if (newDuckOutfit != null) {
                return new ResponseEntity<DuckOutfit>(newDuckOutfit, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete the accecories of a gien duck with the given id
     * 
     * @param id The id of the duck to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DuckOutfit> deleteDuckOutfit(@PathVariable int id) {
        LOG.info("DELETE /outfit/" + id);
        try {
            DuckOutfit noAccessories = new DuckOutfit(0, 0, 0, 0, 0);
            duckDao.getDuck(id).setOutfit(noAccessories);
            if (duckDao.deleteDuck(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
