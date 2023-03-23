package com.ducks.api.ducksapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducks.api.ducksapi.model.Duck;
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
     * Checks out a shopping cart; UPDATES the CART and the INVENTORY
     * 
     * @param id The id of the cart
     * @return 200 if the cart has only valid items
     *         422 if the cart is empty or contains invalid items
     *         404 if the cart does not exist
     *         500 if the cartDao or duckDao fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCart> checkout(@PathVariable int id) {
        LOG.info("GET /cart/checkout/" + id);
        try {
            ShoppingCart cart = cartDao.getShoppingCart(id);
            // 404
            if (cart == null) {
                return new ResponseEntity<ShoppingCart>(HttpStatus.NOT_FOUND);
            }

            Map<String, Duck> invalidItems = getInvalidItems(cart);
            // 422
            if (!invalidItems.isEmpty() || cart.getItems().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Loops through cart items
            for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
                // This must be a number for the cart to be valid
                int duckId = Integer.parseInt(entry.getKey());
                int quantityReq = entry.getValue();

                // Duck can not be null for the cart to be valid
                Duck duck = duckDao.getDuck(duckId);
                duck.setQuantity(duck.getQuantity() - quantityReq);
                duckDao.updateDuck(duck);
            }

            // Clear the items and update the cart
            cart.setItems(new HashMap<>());
            cartDao.updateShoppingCart(cart);

            // 200
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException | NullPointerException | NumberFormatException exc) {
            // Realisitically this NPE and NFE should never be thrown, but to prevent
            // any possibility of runtime crashes, I am catching them
            LOG.log(Level.SEVERE, exc.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validates a given shopping cart; does NOT update the cart in the dao
     * 
     * @param id The id of the cart
     * @return 200 if the cart has no invalid items
     *         200 + new cart object if the cart has invalid items
     *         404 if the cart does not exist
     *         500 if the dao fails
     */
    @GetMapping("/validate/{id}")
    public ResponseEntity<ShoppingCart> validateCart(@PathVariable int id) {
        LOG.info("GET /cart/checkout/validate/" + id);
        try {
            ShoppingCart cart = cartDao.getShoppingCart(id);
            // 404
            if (cart == null) {
                return new ResponseEntity<ShoppingCart>(HttpStatus.NOT_FOUND);
            }

            Map<String, Duck> invalidItems = getInvalidItems(cart);
            // 200
            if (invalidItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            // 200 + new cart object
            Map<String, Integer> cartItems = cart.getItems();
            Map<String, Integer> newCartItems = new HashMap<>();

            // Loops through the cart's items
            for (Map.Entry<String, Integer> cartEntry : cartItems.entrySet()) {
                String cartDuckIdStr = cartEntry.getKey();
                int cartQuantity = cartEntry.getValue();

                // Item is not invalid
                if (!invalidItems.containsKey(cartDuckIdStr)) {
                    newCartItems.put(cartDuckIdStr, cartQuantity);
                    continue;
                }

                Duck invalidDuck = invalidItems.get(cartDuckIdStr);
                // Either Id is not a number or duck is no longer available
                // Realistically, id should always be a number
                if (invalidDuck == null) {
                    continue;
                }

                // Item's requested quantity exceeds quantity available, so the amount requested
                // will be changed to the amount available
                newCartItems.put(cartDuckIdStr, invalidDuck.getQuantity());
            }

            ShoppingCart returnCart = new ShoppingCart(cart.getCustomerId(), newCartItems);
            return new ResponseEntity<ShoppingCart>(returnCart, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            // 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all invalid items in a cart. An invalid item is an item that
     * is either no longer available in the inventory or has a quantity requested
     * that exceeds the quantity available in the inventory
     * 
     * @param cart The cart being checked
     * @return All of the invalid items in the cart, if any
     */
    protected Map<String, Duck> getInvalidItems(ShoppingCart cart) throws IOException {
        Map<String, Integer> itemsMap = cart.getItems();
        // False is map is empty
        if (itemsMap.isEmpty()) {
            return new HashMap<String, Duck>();
        }

        Map<String, Duck> invalidItems = new HashMap<String, Duck>();
        // Loops through the invalidItems map
        for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
            String duckIdStr = entry.getKey();
            int quantity = entry.getValue();

            int duckId;
            try {
                duckId = Integer.parseInt(duckIdStr);
            } catch (NumberFormatException ex) {
                // Invalid if id is not a numbeer
                invalidItems.put(duckIdStr, null);
                continue;
            }

            Duck duck = duckDao.getDuck(duckId);
            // Invalid if duck does not exist
            if (duck == null) {
                invalidItems.put(duckIdStr, null);
                continue;
            }

            // Invalid if the requested quantity exceeds the quantity available
            if (duck.getQuantity() < quantity) {
                invalidItems.put(duckIdStr, duck);
                continue;
            }
        }
        return invalidItems;
    }
}
