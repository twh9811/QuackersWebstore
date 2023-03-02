package com.ducks.api.ducksapi.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a ShoppingCart entity
 * 
 * @author Mason Bausenwein
 */
public class ShoppingCart {

    protected static final String FORMAT = "ShoppingCart [customerId=%s, items=[%s]]";

    @JsonProperty("customerId")
    private final int customerId;

    @JsonProperty("items")
    private Map<Integer, Integer> items;

    /**
     * Creates a new shopping cart with the given item list
     * 
     * @param customerId The customer id that the shopping cart belongs to
     * @param items      The items to add to the shopping cart
     */
    public ShoppingCart(@JsonProperty("customerId") int customerId,
            @JsonProperty("items") Map<Integer, Integer> items) throws IllegalArgumentException {
        this.customerId = customerId;

        if (!isItemsMapValid(items)) {
            String exMsg = "The items map can not contain values which are less than or equal to 0";
            throw new IllegalArgumentException(exMsg);
        }
        this.items = items;
    }

    /**
     * Creates a new shopping cart with an empty item list
     * 
     * @param customerId The customer id that the shopping cart belongs to
     */
    public ShoppingCart(int customerId) {
        this(customerId, new HashMap<Integer, Integer>());
    }

    /**
     * @return Returns the customer id of which this shopping cart belongs to
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets all of the items (ducks) in the shopping cart
     * 
     * @return An set of duck ids
     */
    public Set<Integer> getItems() {
        return items.keySet();
    }

    /**
     * Gets the amount of an item in a shopping cart
     * 
     * @param duck The duck for which the quantity is being retrieved
     * @return The amount of the item in the cart
     * @throws IllegalArgumentException If the duck is null, or if thrown by
     *                                  getItemAmount(int)
     */
    public int getItemAmount(Duck duck) throws IllegalArgumentException {
        if (duck == null) {
            throw new IllegalArgumentException("The passed duck can not be null");
        }

        return getItemAmount(duck.getId());
    }

    /**
     * Gets the amount of an item in a shopping cart
     * 
     * @param duckId The id of the duck
     * @return The amount of the item in the shopping cart
     * @throws IllegalArgumentException If the cart does not contain a duck with the
     *                                  id
     */
    public int getItemAmount(Integer duckId) throws IllegalArgumentException {
        if (!this.items.containsKey(duckId)) {
            throw new IllegalArgumentException("There is no duck with the id " + duckId + " in the shopping cart");
        }

        return this.items.get(duckId);
    }

    /**
     * Adds one of each of the given ducks to the shopping cart
     * 
     * @param ducks The Duck objects to add
     * @throws IllegalArgumentException If thrown by addItems(int...)
     */
    public void addItems(Duck... ducks) throws IllegalArgumentException {
        Integer[] duckIds = convertDuckArrayToIdArray(ducks);
        addItems(duckIds);
    }

    /**
     * Adds one of each given duck id to the cart
     * 
     * @param duckIds The duck ids to add
     * @throws IllegalArgumentException If the duck id array is empty
     */
    public void addItems(Integer... duckIds) throws IllegalArgumentException {
        if (duckIds.length == 0) {
            throw new IllegalArgumentException("There must be at least one duck id in the entered array");
        }

        for (Integer duckId : duckIds) {
            int quantity = items.containsKey(duckId) ? items.get(duckId) + 1 : 1;
            items.put(duckId, quantity);
        }
    }

    /**
     * Adds a given amount of a given duck to the shopping cart
     * 
     * @param duck     The duck being added to the cart
     * @param quantity The amount of the duck being added to the cart
     * @throws IllegalArgumentException If the duck is null or if thrown by
     *                                  addItemAmount(int, int)
     */
    public void addItemAmount(Duck duck, int quantity) throws IllegalArgumentException {
        if (duck == null) {
            throw new IllegalArgumentException("The passed duck can not be null");
        }

        addItemAmount(duck.getId(), quantity);
    }

    /**
     * Adds a given amount of a given duck to the shopping cart
     * 
     * @param duckId   The duck id being added to the cart
     * @param quantity The amount of the duck being added to the cart
     * @throws IllegalArgumentException If the quantity is less than or equal to 0
     */
    public void addItemAmount(int duckId, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("The entered quantity must be greater than 0");
        }

        int newQuantity = items.containsKey(duckId) ? items.get(duckId) + quantity : quantity;
        items.put(duckId, newQuantity);
    }

    /**
     * Removes the given ducks from the shopping cart
     * 
     * @param ducks The duck objects to remove
     * @throws IllegalArgumentException If thrown by removeItems(int...)
     */
    public void removeItems(Duck... ducks) throws IllegalArgumentException {
        Integer[] duckIds = convertDuckArrayToIdArray(ducks);
        removeItems(duckIds);
    }

    /**
     * Removes the given duck ids from the shopping cart
     * 
     * @param duckIds The duck ids to remove
     * @throws IllegalArgumentException If one of the ducks does not exist in the
     *                                  shopping cart, or if the duck id array is
     *                                  empty
     */
    public void removeItems(Integer... duckIds) throws IllegalArgumentException {
        if (duckIds.length == 0) {
            throw new IllegalArgumentException("There must be at least one duck id in the entered array");
        }

        for (Integer duckId : duckIds) {
            if (!this.items.containsKey(duckId)) {
                throw new IllegalArgumentException("There is no duck with the id " + duckId + " in the shopping cart");
            }
            this.items.remove(duckId);
        }
    }

    /**
     * Removes a set amount of an item
     * 
     * @param duck     The duck being removed
     * @param quantity The amount of the duck to remove
     * @throws IllegalArgumentException If the duck is null or if thrown by
     *                                  removeItemAmount(int, int)
     */
    public void removeItemAmount(Duck duck, int quantity) throws IllegalArgumentException {
        if (duck == null) {
            throw new IllegalArgumentException("The passed duck can not be null");
        }
        removeItemAmount(duck.getId(), quantity);
    }

    /**
     * Removes a set amount of an item
     * 
     * @param duckId   The duck id to remove
     * @param quantity The amount of the duck to remove
     * @throws IllegalArgumentException If there is no duck with the given id in the
     *                                  shopping cart.
     *                                  If the quantity attempt to be removed is
     *                                  less than or equal to 0.
     *                                  If the quantity attempting to be removed
     *                                  exceeds the amount in the cart.
     */
    public void removeItemAmount(int duckId, int quantity) throws IllegalArgumentException {
        if (!this.items.containsKey(duckId)) {
            throw new IllegalArgumentException("There is no duck with the id " + duckId + " in the shopping cart");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("The entered quantity must be greater than 0");
        }

        int shoppingQuantity = this.items.get(duckId);
        if (shoppingQuantity < quantity) {
            String exFormat = "Attempted to remove %d of the duck with the id %d but only %d exist in the shopping cart";
            throw new IllegalArgumentException(String.format(exFormat, quantity, duckId, shoppingQuantity));
        }

        if (shoppingQuantity - quantity == 0) {
            this.items.remove(duckId);
            return;
        }

        this.items.put(duckId, shoppingQuantity - quantity);
    }

    /**
     * Removes a duck based on a given id
     * 
     * @param duckId The id of the duck being removed
     * @throws IllegalArgumentException If there is no duck in the shopping cart
     *                                  with the passed id
     */
    public void removeItemById(int duckId) throws IllegalArgumentException {
        boolean didDelete = this.items.remove(duckId) == duckId;
        if (didDelete)
            return;

        String errorFormat = "No duck with the id %d found in the shopping cart with the customerId of %d";
        throw new IllegalArgumentException(String.format(errorFormat, duckId, this.customerId));
    }

    /**
     * Clears the items in the shopping cart
     */
    public void clearItems() {
        this.items.clear();
    }

    /**
     * Converts an array of ducks to an array of their ids. Also removes all null
     * entries
     * 
     * @param ducks The duck array being converted
     * @return The newly created array of ids
     */
    private Integer[] convertDuckArrayToIdArray(Duck[] ducks) {
        return Arrays.asList(ducks)
                .stream()
                .filter(duck -> duck != null)
                .map(Duck::getId)
                .toArray(Integer[]::new);
    }

    /**
     * Checks if an items map is valid. An items map is considered valid if all
     * values are greater than 0
     * 
     * @param itemsMap The map being checked
     * @return true if valid
     */
    private boolean isItemsMapValid(Map<Integer, Integer> itemsMap) {
        long invalidItemCount = itemsMap.values()
                .stream()
                .filter(value -> (value <= 0))
                .count();
        return invalidItemCount == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShoppingCart)) {
            return false;
        }

        ShoppingCart other = (ShoppingCart) obj;
        return this.customerId == other.customerId && this.items.equals(other.items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // Converts each map entry to a string in the form key=value
        // Then, joins them on ', '
        String itemsString = items.entrySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        return String.format(FORMAT, this.customerId, itemsString);
    }

}
