package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.info.InfoProperties;
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

    /**
     * Responds to the GET request for all {@linkplain ShoppingCart Shopping carts}
     * 
     * @return ResponseEntity with array of {@link ShoppingCart Shopping carts} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<ShoppingCart[]> getShoppingCarts(){
        LOG.info("GET /shopping");
        try{
            ShoppingCart[] carts = cartDao.getShoppingCarts();
            if(carts != null) {
                return new ResponseEntity<>(carts, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(carts, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch(IOException ioe) {
        LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PostMapping("")
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart cart) {
        LOG.info("POST /shopping " + cart);
        try{
            ShoppingCart newCart = cartDao.createShoppingCart(cart);
            if (newCart != null) {
                return new ResponseEntity<ShoppingCart>(newCart,HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
}
