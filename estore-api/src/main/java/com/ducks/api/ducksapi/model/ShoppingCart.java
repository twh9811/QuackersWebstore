package com.ducks.api.ducksapi.model;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<Duck> items;

    /**
     * Creates a new shopping cart with the given item list
     * 
     * @param customerId The customer id that the shopping cart belongs to
     * @param items      The items to add to the shopping cart
     */
    public ShoppingCart(int customerId, ArrayList<Duck> items) {
        this.customerId = customerId;
        this.items = items;
    }

    /**
     * Creates a new shopping cart with an empty item list
     * 
     * @param customerId The customer id that the shopping cart belongs to
     */
    public ShoppingCart(int customerId) {
        this(customerId, new ArrayList<Duck>());
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
     * @return An ArrayList of ducks
     */
    public ArrayList<Duck> getItems() {
        return items;
    }

    /**
     * Adds given ducks to the shopping cart
     * 
     * @param itemsToAdd The Duck objects to remove
     */
    public void addItems(Duck... itemsToAdd) {
        this.items.addAll(Arrays.asList(itemsToAdd));
    }

    /**
     * Removes the given ducks from the shopping cart
     * 
     * @param itemsToRemove The duck objects to remove
     */
    public void removeItems(Duck... itemsToRemove) {
        this.items.removeAll(Arrays.asList(itemsToRemove));
    }

    /**
     * Clears the items in the shopping cart
     */
    public void clearItems() {
        this.items.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShoppingCart))
            return false;

        ShoppingCart other = (ShoppingCart) obj;
        return this.customerId == other.customerId && this.items.equals(other.items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // Joins the items list to a string delimited by ', '
        String itemsString = items.stream().map(Duck::toString).collect(Collectors.joining(", "));
        return String.format(FORMAT, this.customerId, itemsString);
    }

}
