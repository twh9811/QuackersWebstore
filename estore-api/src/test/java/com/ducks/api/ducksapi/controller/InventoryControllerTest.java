package com.ducks.api.ducksapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ducks.api.ducksapi.model.Colors;
import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.DuckOutfit;
import com.ducks.api.ducksapi.model.Size;
import com.ducks.api.ducksapi.persistence.DuckDAO;

/**
 * Test the Duck Controller class
 * 
 * @author SWEN Faculty, SWEN-261-06 Team 8
 */
@Tag("Controller-tier")
public class InventoryControllerTest {
    private static final Random RANDOM = new Random();

    private InventoryController duckController;
    private DuckDAO mockDuckDAO;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock Duck DAO
     */
    @BeforeEach
    public void setupInventoryController() {
        mockDuckDAO = mock(DuckDAO.class);
        duckController = new InventoryController(mockDuckDAO);
    }

    /**
     * Generates a duck with completely RANDOM values
     * @return The generated duck
     */
    private Duck generateDuck() {
        String name = String.valueOf(RANDOM.nextInt(100));
        int id = RANDOM.nextInt(1000);
        int quantiy = RANDOM.nextInt(1000);
        String price = String.valueOf(RANDOM.nextInt(1000));
        Size size = Size.values()[RANDOM.nextInt(Size.values().length)];
        Colors color = Colors.values()[RANDOM.nextInt(Colors.values().length)];
        DuckOutfit outfit = new DuckOutfit(RANDOM.nextInt(20), RANDOM.nextInt(20), RANDOM.nextInt(20), RANDOM.nextInt(20),RANDOM.nextInt(20));

        return new Duck(id, name, quantiy, price, size, color, outfit);
    }

    @Test
    public void testGetDuck() throws IOException { // getDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // When the same id is passed in, our mock Duck DAO will return the Duck object
        when(mockDuckDAO.getDuck(duck.getId())).thenReturn(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.getDuck(duck.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(duck, response.getBody());
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
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all InventoryController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateDuck() throws IOException { // createDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // when createDuck is called, return true simulating successful
        // creation and save
        when(mockDuckDAO.createDuck(duck)).thenReturn(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(duck, response.getBody());
    }

    @Test
    public void testCreateDuckFailed() throws IOException { // createDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // when createDuck is called, return false simulating failed
        // creation and save
        when(mockDuckDAO.createDuck(duck)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateDuckHandleException() throws IOException { // createDuck may throw IOException
        // Setup
        Duck duck = generateDuck();

        // When createDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).createDuck(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.createDuck(duck);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateDuck() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // when updateDuck is called, return true simulating successful
        // update and save
        when(mockDuckDAO.updateDuck(duck)).thenReturn(duck);
        
        // Update Duck
        duck.setName("Bolt");

        // Invoke
        ResponseEntity<Duck> response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(duck, response.getBody());
    }

    @Test
    public void testUpdateDuckFailed() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // when updateDuck is called, return true simulating successful
        // update and save
        when(mockDuckDAO.updateDuck(duck)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck> response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateDuckHandleException() throws IOException { // updateDuck may throw IOException
        // Setup
        Duck duck = generateDuck();
        // When updateDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).updateDuck(duck);

        // Invoke
        ResponseEntity<Duck> response = duckController.updateDuck(duck);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetDucks() throws IOException { // getDucks may throw IOException
        // Setup
        Duck[] ducks = new Duck[2];
        ducks[0] = generateDuck();
        ducks[1] = generateDuck();
        // When getDucks is called return the ducks created above
        when(mockDuckDAO.getDucks()).thenReturn(ducks);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.getDucks();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ducks, response.getBody());
    }

    @Test
    public void testGetDucksNoDucks() throws IOException {
        // When getDucks is called return the ducks created above
        when(mockDuckDAO.getDucks()).thenReturn(null);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.getDucks();

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetDucksHandleException() throws IOException { // getDucks may throw IOException
        // Setup
        // When getDucks is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).getDucks();

        // Invoke
        ResponseEntity<Duck[]> response = duckController.getDucks();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchDucks() throws IOException { // findDucks may throw IOException
        // Setup
        String searchString = "la";
        Duck[] ducks = new Duck[2];
        ducks[0] = generateDuck();
        ducks[1] = generateDuck();
        // When findDucks is called with the search string, return the two
        /// ducks above
        when(mockDuckDAO.findDucks(searchString)).thenReturn(ducks);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ducks, response.getBody());
    }

    @Test
    public void testSearchDucksNotFound() throws IOException { // findDucks may throw IOException
        // Setup
        String searchString = "la";
        // When findDucks is called with the search string, return the two
        /// ducks above
        when(mockDuckDAO.findDucks(searchString)).thenReturn(new Duck[0]);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testSearchDucksNull() throws IOException { // findDucks may throw IOException
        // Setup
        String searchString = "la";
        // When findDucks is called with the search string, return the two
        /// ducks above
        when(mockDuckDAO.findDucks(searchString)).thenReturn(null);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testSearchDucksHandleException() throws IOException { // findDucks may throw IOException
        // Setup
        String searchString = "an";
        // When createDuck is called on the Mock Duck DAO, throw an IOException
        doThrow(new IOException()).when(mockDuckDAO).findDucks(searchString);

        // Invoke
        ResponseEntity<Duck[]> response = duckController.searchDucks(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
