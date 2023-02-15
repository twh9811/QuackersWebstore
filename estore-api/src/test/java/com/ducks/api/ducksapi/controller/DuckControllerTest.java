package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.controller.DuckController;
import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.persistence.DuckDAO;

/**
 * Test the Duck Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class DuckControllerTest {
    private DuckController duckController;
    private DuckDAO mockDuckDAO;

    /**
     * Before each test, create a new DuckController object and inject
     * a mock Duck DAO
     */
    @BeforeEach
    public void setupDuckController() {
        mockDuckDAO = mock(DuckDAO.class);
        duckController = new DuckController(mockDuckDAO);
    }

    @Test
    public void testGetDuck() throws IOException {  // getDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Galactic Agent");
        // When the same id is passed in, our mock Duck DAO will return the Duck object
        when(mockDuckDAO.getDuck(duck.getId())).thenReturn(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.getDuck(duck.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(duck,response.getBody());
    }

    @Test
    public void testGetDuckNotFound() throws Exception { // createDuck may throw IOException
        // Setup
        int duckId = 99;
        // When the same id is passed in, our mock Duck DAO will return null, simulating
        // no duck found
        when(mockDuckDAO.getDuck(duckId)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck> response = duckController.getDuck(duckId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetDuckHandleException() throws Exception { // createDuck may throw IOException
        // Setup
        int duckId = 99;
        // When getDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).getDuck(duckId);

        // Invoke
        ResponseEntity<Duck> response = duckController.getDuck(duckId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all DuckController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateDuck() throws IOException {  // createDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Wi-Fire");
        // when createDuck is called, return true simulating successful
        // creation and save
        when(mockDuckDAO.createDuck(duck)).thenReturn(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(duck,response.getBody());
    }

    @Test
    public void testCreateDuckFailed() throws IOException {  // createDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Bolt");
        // when createDuck is called, return false simulating failed
        // creation and save
        when(mockDuckDAO.createDuck(duck)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateDuckHandleException() throws IOException {  // createDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Ice Gladiator");

        // When createDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).createDuck(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateDuck() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Wi-Fire");
        // when updateDuck is called, return true simulating successful
        // update and save
        when(mockDuckDAO.updateDuck(duck)).thenReturn(duck);
        ResponseEntity<Duck> response = duckController.updateDuck(duck);
        duck.setName("Bolt");

        // Invoke
        response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(duck,response.getBody());
    }

    @Test
    public void testUpdateDuckFailed() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Galactic Agent");
        // when updateDuck is called, return true simulating successful
        // update and save
        when(mockDuckDAO.updateDuck(duck)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck> response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateDuckHandleException() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = new Duck(99,"Galactic Agent");
        // When updateDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).updateDuck(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetDuckes() throws IOException { // getDuckes may throw IOException
        // Setup
        Duck[] duckes = new Duck[2];
        duckes[0] = new Duck(99,"Bolt");
        duckes[1] = new Duck(100,"The Great Iguana");
        // When getDuckes is called return the duckes created above
        when(mockDuckDAO.getDucks()).thenReturn(duckes);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.getDucks();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(duckes,response.getBody());
    }

    @Test
    public void testGetDuckesHandleException() throws IOException { // getDuckes may throw IOException
        // Setup
        // When getDuckes is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).getDucks();

        // Invoke
        ResponseEntity<Duck[]> response = duckController.getDucks();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchDuckes() throws IOException { // findDuckes may throw IOException
        // Setup
        String searchString = "la";
        Duck[] duckes = new Duck[2];
        duckes[0] = new Duck(99,"Galactic Agent");
        duckes[1] = new Duck(100,"Ice Gladiator");
        // When findDuckes is called with the search string, return the two
        /// duckes above
        when(mockDuckDAO.findDucks(searchString)).thenReturn(duckes);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(duckes,response.getBody());
    }

    @Test
    public void testSearchDuckesHandleException() throws IOException { // findDuckes may throw IOException
        // Setup
        String searchString = "an";
        // When createDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).findDucks(searchString);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteDuck() throws IOException { // deleteDuck may throw IOException
        // Setup
        int duckId = 99;
        // when deleteDuck is called return true, simulating successful deletion
        when(mockDuckDAO.deleteDuck(duckId)).thenReturn(true);

        // Invoke
        ResponseEntity<Duck> response = duckController.deleteDuck(duckId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteDuckNotFound() throws IOException { // deleteDuck may throw IOException
        // Setup
        int duckId = 99;
        // when deleteDuck is called return false, simulating failed deletion
        when(mockDuckDAO.deleteDuck(duckId)).thenReturn(false);

        // Invoke
        ResponseEntity<Duck> response = duckController.deleteDuck(duckId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteDuckHandleException() throws IOException { // deleteDuck may throw IOException
        // Setup
        int duckId = 99;
        // When deleteDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).deleteDuck(duckId);

        // Invoke
        ResponseEntity<Duck> response = duckController.deleteDuck(duckId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
