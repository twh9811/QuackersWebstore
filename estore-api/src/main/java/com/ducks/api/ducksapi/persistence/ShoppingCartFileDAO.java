package com.ducks.api.ducksapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ducks.api.ducksapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShoppingCartFileDAO implements ShoppingCartDAO {

    private static final Logger LOG = Logger.getLogger(DuckFileDAO.class.getName());
    Map<Integer, ShoppingCart> carts; // Provides a local cache of the shopping cart objects
                                      // so that we don't need to read from the file
                                      // each time
    private ObjectMapper objectMapper; // Provides conversion between Duck
                                       // objects and JSON text format written
                                       // to the file
    private String filename; // Filename to read from and write to

    /**
     * Creates a ShoppingCart File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public ShoppingCartFileDAO(@Value("${carts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the shopping carts from the file
    }

    /**
     * Saves the {@linkplain ShoppingCart carts} from the map into the file as an
     * array of
     * JSON objects
     * 
     * @return true if the {@link ShoppingCart carts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        ShoppingCart[] cartArray = getShoppingCarts();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), cartArray);
        return true;
    }

    /**
     * Loads {@linkplain ShoppingCart carts} from the JSON file into the map
     * <br>
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        carts = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of carts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        ShoppingCart[] cartArray = objectMapper.readValue(new File(filename), ShoppingCart[].class);

        // Add each cart to the tree map and keep track of the greatest id
        for (ShoppingCart cart : cartArray) {
            carts.put(cart.getCustomerId(), cart);
        }

        return true;
    }

    /**
     * Generates an array of {@linkplain ShoppingCart carts} from the tree map for
     * each
     * {@linkplain ShoppingCart carts} in the tree map
     * 
     * @return The array of {@link ShoppingCart carts}, may be empty
     */
    private ShoppingCart[] getShoppingCartArray() {
        ArrayList<ShoppingCart> cartArrayList = new ArrayList<>();

        for (ShoppingCart cart : carts.values()) {
            cartArrayList.add(cart);
        }

        ShoppingCart[] cartArray = new ShoppingCart[cartArrayList.size()];
        cartArrayList.toArray(cartArray);
        return cartArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCart createShoppingCart(ShoppingCart cart) throws IOException {
        synchronized (carts) {
            // Checks if a customer already has a shopping cart
            if (carts.get(cart.getCustomerId()) != null) {
                return null;
            }

            carts.put(cart.getCustomerId(), cart);
            save();
            return cart;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteShoppingCart(int id) throws IOException {
        synchronized (carts) {
            if (carts.get(id) == null) {
                return false;
            }

            carts.remove(id);
            return save();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCart getShoppingCart(int id) {
        synchronized (carts) {
            return carts.get(id); // NULL If not found
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCart[] getShoppingCarts() {
        synchronized (carts) {
            return getShoppingCartArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart cart) throws IOException {
        synchronized (carts) {
            if (carts.get(cart.getCustomerId()) == null) {
                return null;
            }

            carts.put(cart.getCustomerId(), cart);
            save();
            return cart;
        }

    }

}
