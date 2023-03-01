package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.Duck;
import com.ducks.api.ducksapi.model.ShoppingCart;
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

@RestController
@RequestMapping("shopping")
public class ShoppingCartController {
    
    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    private ShoppingCartDAO cartDao;

    /**
     * Creates a REST API controller to reponds to shopping cart requests
     * 
     * @param cartDao The {@link ShoppingCatDAO Cart Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public ShoppingCartController(ShoppingCartDAO cartDao) {
        this.cartDao = cartDao;
    }

    // TODO: Change this.
    @GetMapping("")
    public ResponseEntity<ShoppingCart> getShoppingCart() {
        LOG.info("GET /shopping");
        try {
            return new ResponseEntity<ShoppingCart>(cartDao.getShoppingCart(2), HttpStatus.I_AM_A_TEAPOT);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
