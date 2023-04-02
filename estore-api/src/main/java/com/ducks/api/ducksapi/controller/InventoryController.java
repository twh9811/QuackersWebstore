package com.ducks.api.ducksapi.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.persistence.DuckDAO;

/**
 * Handles the REST API requests for the Duck resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty, SWEN-261-06 Team 8
 */

@RestController
@RequestMapping("inventory")
public class InventoryController extends AbstractInventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param duckDao The {@link DuckDAO Duck Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public InventoryController(@Qualifier("duckFileDAO") DuckDAO duckDao) {
        super(duckDao, LOG);
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
    @GetMapping("/product/{id}")
    public ResponseEntity<Duck> getDuck(@PathVariable int id) {
        return super.getDuck(id, "GET /inventory/product/{0}");
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
    @GetMapping("")
    public ResponseEntity<Duck[]> getDucks() {
        return super.getDucks("GET /inventory");
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
    @GetMapping("/search")
    public ResponseEntity<Duck[]> searchDucks(@RequestParam String name) {
        return super.searchDucks(name, "GET /inventory/search?name={0}");
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
    @PostMapping("/product")
    public ResponseEntity<Duck> createDuck(@RequestBody Duck duck) {
        return super.createDuck(duck, "POST /inventory/product {0}");
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
    @PutMapping("/product")
    public ResponseEntity<Duck> updateDuck(@RequestBody Duck duck) {
        return super.updateDuck(duck, "PUT /inventory {0}");
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
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Duck> deleteDuck(@PathVariable int id) {
        return super.deleteDuck(id, "DELETE /inventory/product/{0}");
    }
}
