package com.ducks.api.ducksapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ducks.api.ducksapi.model.Colors;
import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.DuckOutfit;
import com.ducks.api.ducksapi.model.Size;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test the Duck File DAO class
 * 
 * @author SWEN Faculty, SWEN-261-06 Team 8
 */
@Tag("Persistence-tier")
public class DuckFileDAOTest {
    DuckFileDAO duckFileDAO;
    Duck[] testDucks;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupDuckFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testDucks = new Duck[4];
        // Modified constructors to test attributes @Travis 2/15
        DuckOutfit outfit = new DuckOutfit(0, 0, 0, 0, 0);
        testDucks[0] = new Duck(99, "Wi-Fire", 12, 9.99, Size.MEDIUM, Colors.BLUE, outfit);
        testDucks[1] = new Duck(100, "Galactic Agent 2", 11, 19.99, Size.SMALL, Colors.RED, outfit);
        testDucks[2] = new Duck(101, "Ice Gladiator", 10, 29.99, Size.EXTRA_LARGE, Colors.GREEN, outfit);
        testDucks[3] = new Duck(102, "Galactic Agent", 10, 29.99, Size.EXTRA_LARGE, Colors.GREEN, outfit);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the duck array above
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Duck[].class))
                .thenReturn(testDucks);
        duckFileDAO = new DuckFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetDucks() {
        // Invoke
        Duck[] ducks = duckFileDAO.getDucks();

        // Analyze
        assertEquals(ducks.length, testDucks.length);
        for (int i = 0; i < testDucks.length; ++i)
            assertEquals(ducks[i], testDucks[i]);
    }

    @Test
    public void testFindDucks() {
        // Invoke
        Duck[] ducks = duckFileDAO.findDucks("la");

        // Analyze
        assertEquals(3, ducks.length);
        assertEquals(testDucks[1], ducks[0]);
        assertEquals(testDucks[2], ducks[1]);
    }

    @Test
    public void testGetDuck() {
        // Invoke
        Duck duck = duckFileDAO.getDuck(99);

        // Analzye
        assertEquals(testDucks[0], duck);
    }

    @Test
    public void testGetDuckNotFound() {
        // Invoke
        Duck duck = duckFileDAO.getDuck(98);

        // Analyze
        assertEquals(null, duck);
    }

    @Test
    public void testGetDuckByName() {
        // Invoke
        Duck duck = duckFileDAO.getDuckByName(testDucks[3].getName());

        // Analyze
        assertEquals(testDucks[3], duck);
    }

    @Test
    public void testGetDuckByNameNonExistant() {
        // Invoke
        Duck duck = duckFileDAO.getDuckByName("I don't exist");

        // Analyze
        assertEquals(null, duck);
    }

    @Test
    public void testDeleteDuck() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> duckFileDAO.deleteDuck(99),
                "Unexpected exception thrown");

        // Analzye
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test ducks array - 1 (because of the delete)
        // Because ducks attribute of DuckFileDAO is package private
        // we can access it directly
        assertEquals(testDucks.length - 1, duckFileDAO.ducks.size());
    }

    @Test
    public void testCreateDuck() {
        // Setup
        int id = testDucks[3].getId() + 1;
        Duck duck = new Duck(id, "Wonder-Person", 10, 9.99, Size.MEDIUM, Colors.BLUE, new DuckOutfit(0, 0, 0, 0, 0));

        // Invoke
        Duck result = assertDoesNotThrow(() -> duckFileDAO.createDuck(duck),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Duck actual = duckFileDAO.getDuck(duck.getId());
        assertEquals(actual.getId(), duck.getId());
        assertEquals(actual.getName(), duck.getName());
    }

    @Test
    public void testUpdateDuck() {
        // Setup
        Duck duck = new Duck(99, "Galactic Agent", 10, 9.99, Size.LARGE, Colors.INDIGO, new DuckOutfit(0, 0, 0, 0, 0));

        // Invoke
        Duck result = assertDoesNotThrow(() -> duckFileDAO.updateDuck(duck),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Duck actual = duckFileDAO.getDuck(duck.getId());
        assertEquals(actual, duck);
    }

    @Test
    public void testCreateDuplicateName() throws IOException {
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Duck[].class));

        Duck duck = new Duck(102, "Wi-Fire", 10, 9.99, Size.SMALL, Colors.ORANGE, new DuckOutfit(0, 0, 0, 0, 0));

        assertNull(duckFileDAO.createDuck(duck), "createDuck did not return null");
    }

    @Test
    public void testDeleteDuckNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> duckFileDAO.deleteDuck(98),
                "Unexpected exception thrown");

        // Analyze
        assertFalse(result);
        assertEquals(testDucks.length, duckFileDAO.ducks.size());
    }

    @Test
    public void testUpdateDuckNotFound() {
        // Setup
        Duck duck = new Duck(98, "Bolt", 10, 9.99, Size.LARGE, Colors.YELLOW, new DuckOutfit(0, 0, 0, 0, 0));

        // Invoke
        Duck result = assertDoesNotThrow(() -> duckFileDAO.updateDuck(duck),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the DuckFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
                .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), Duck[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                () -> new DuckFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }
}
