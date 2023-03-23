package com.ducks.api.ducksapi.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.ShoppingCart;
import com.ducks.api.ducksapi.persistence.DuckDAO;
import com.ducks.api.ducksapi.persistence.ShoppingCartDAO;

/**
 * Handles the REST API requests for the Checkout resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Mason Bausenwein
 */

@RestController
@RequestMapping("cart/checkout")
public class CheckoutController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private ShoppingCartDAO cartDao;
    private DuckDAO duckDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param cartDao The {@link ShoppingCatDAO Cart Data Access Object} to
     *                perform
     *                CRUD operations<br>
     *                This dependency is injected by the Spring Framework
     * 
     * @param duckDAO The {@link DuckDAO Duck Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public CheckoutController(ShoppingCartDAO cartDAO, DuckDAO duckDAO) {
        this.cartDao = cartDAO;
        this.duckDao = duckDAO;
    }

    /**
     * Checksout a shopping cart; UPDATES the CART and the INVENTORY
     * 
     * @param id The id of the cart
     * @return 200 if the cart has only valid items
     *         400 if the cart is empty
     *         403 if the cart contains invalid items
     *         404 if the cart does not exist
     *         500 if the cartDao or duckDao fails
     */
    @PutMapping("/")
    public ResponseEntity<ShoppingCart> checkout(@PathVariable int id) {
        LOG.info("GET /cart/checkout/" + id);
        return null;
    }

    /**
     * Validates a given shopping cart; does NOT update the cart in the dao
     * 
     * @param id The id of the cart
     * @return 200 if the cart has no invalid items
     *         200 + new cart object if the cart has invalid items
     *         409 if the cart does not exist
     *         500 if the dao fails
     */
    @GetMapping("/validate")
    public ResponseEntity<ShoppingCart> validateCart(@PathVariable int id) {
        LOG.info("GET /cart/checkout/validate/" + id);
        return null;
    }
}
