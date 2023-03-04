package com.ducks.api.ducksapi.controller;

import org.springframework.http.HttpStatus;
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
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ShoppingCartController {
    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    
    private ShoppingCartDAO shoppingCartDAO;

    public ShoppingCartController(ShoppingCartDAO shoppingCartDAO){
        this.shoppingCartDAO = shoppingCartDAO;
    }

    
}
