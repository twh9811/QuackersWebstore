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

@RestController
@RequestMapping("customize")
public class CustomizeController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private DuckDAO duckDao;
     
    public CustomizeController(@Qualifier("customDuckFileDAO") DuckDAO duckDao){
        this.duckDao = duckDao;
    }

    @GetMapping("/customize/{id}")
    public ResponseEntity<Duck> getDuckOutfit()(@PathVariable int id) {
        LOG.info("GET /customize/" + id);
        try {
            DuckOutfit duckOutfit = duckDao.getDuck(id).duckOutfit;
            if (duckOutfit != null) {
                return new ResponseEntity<DuckOutfit>(duckOutfit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<Duck[]> getDucksOutfit() {
        LOG.info("GET /customize");
        try {
            Duck[] ducks = duckDao.getDucks();
            if (ducks != null && ducks.length != 0) {
                return new ResponseEntity<Duck[]>(ducks, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
