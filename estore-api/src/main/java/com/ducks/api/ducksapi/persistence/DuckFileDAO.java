package com.ducks.api.ducksapi.persistence;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implements the functionality for JSON file-based peristance for Ducks
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component("duckFileDAO")
public class DuckFileDAO extends DuckFileDAOAbstract {
    
    /**
     * Creates a Duck File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public DuckFileDAO(@Value("${ducks.file}") String filename, ObjectMapper objectMapper) throws IOException {
        super(filename, objectMapper);
    }

}
