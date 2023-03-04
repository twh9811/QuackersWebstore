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


/**
 * Handles the REST API requests for the Shopping Cart resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Andrew Le
 */

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

    /**
     * Retrieves a {@linkplain ShoppingCart shopping cart} with the given customer id
     * 
     * @param id The id of the {@linkplain ShoppingCart shopping cart} to get
     * 
     * @return a {@linkplain ShoppingCart shopping cart} object with the matching id
     * <br>
     * null if no {@linkplain ShoppingCart shopping cart} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    // TODO: Change this.
    @GetMapping("/shopping/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable int id) {
        LOG.info("GET /shopping/" + id);
        try {
            ShoppingCart cart = cartDao.getShoppingCart(id);
            if (cart != null) {
                return new ResponseEntity<ShoppingCart>(cart,HttpStatus.OK);
            } else {
                return new ResponseEntity<ShoppingCart>(HttpStatus.NOT_FOUND);
            }
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

    /**
     * Creates a {@linkplain ShoppingCart shoppingCart}
     * @param hero - The {@link ShoppingCart shoppingCart} to create
     * 
     * @return ResponseEntity with created {@link ShoppingCart shoppingCart} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link ShoppingCart shoppingCart} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
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

    /**
     * Updates the {@linkplain ShoppingCart shoppingCart} with the provided {@linkplain ShoppingCart shoppingCart} object, if it exists
     * 
     * @param cart The {@link ShoppingCart shoppingCart} to update
     * 
     * @return ResponseEntity with updated {@link ShoppingCart shoppingCart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<ShoppingCart> updateShoppingCart(ShoppingCart cart) {
        LOG.info("PUT /shopping " + cart);
        try {
            ShoppingCart updateCart = cartDao.updateShoppingCart(cart);
            if(updateCart != null) {
                return new ResponseEntity<ShoppingCart>(updateCart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Deletes a {@linkplain ShoppingCart shoppingCart} with the given id
     * 
     * @param id The id of the {@link ShoppingCart shoppingCart} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Duck> deleteShoppingCart(@PathVariable int id) {
        LOG.info("DELETE /shopping/cart/" + id);
        try{
            if(cartDao.deleteShoppingCart(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}   


