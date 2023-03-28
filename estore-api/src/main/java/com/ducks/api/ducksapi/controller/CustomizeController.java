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

import com.ducks.api.ducksapi.model.DuckOutfit;
import com.ducks.api.ducksapi.persistence.DuckDAO;

@RestController
@RequestMapping("customize")
public class CustomizeController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private DuckDAO duckDao;
     
    public CustomizeController(DuckDAO duckDao){
        this.duckDao = duckDao;
    }

    @GetMapping("/customize/{id}")
    
}
