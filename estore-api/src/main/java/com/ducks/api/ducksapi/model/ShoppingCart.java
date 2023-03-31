package com.ducks.api.ducksapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
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
    private Map<String, Integer> items;

    /**
     * Creates a new shopping cart with the given item list
     * 
     * @param customerId The customer id that the shopping cart belongs to
     * @param items      The items to add to the shopping cart
     */
    @JsonCreator
    public ShoppingCart(@JsonProperty("customerId") int customerId, @JsonProperty("items") Map<String, Integer> items)
            throws IllegalArgumentException {
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
        this(customerId, new HashMap<>());
    }

    /**
     * @return Returns the customer id of which this shopping cart belongs to
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets the items map
     * 
     * @return The items map
     */
    public Map<String, Integer> getItems() {
        return items;
    }

    /**
     * Sets the items map
     * 
     * @param items The new map
     */
    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    /**
     * Checks if an items map is valid. An items map is considered valid if all
     * values are greater than 0
     * 
     * @param itemsMap The map being checked
     * @return true if valid
     */
    private boolean isItemsMapValid(Map<String, Integer> itemsMap) {
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