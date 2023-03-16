package com.ducks.api.ducksapi.model;

import java.beans.Transient;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Duck entity
 * 
 * @author SWEN Faculty, SWEN-261-06 Team 8
 */
public class Duck {
    private static final Logger LOG = Logger.getLogger(Duck.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Duck [id=%d, name=%s, quantity=%d, price=%s, size=%s, color=%s, hatUID=%d, shirtUID=%d, shoesUID=%d, handitemUID=%d, jewelryUID=%d]";

    @JsonProperty("id")
    private final int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    private double price;

    @JsonProperty("size")
    private Size size;

    @JsonProperty("color")
    private Colors color;

    @JsonProperty("outfit")
    private DuckOutfit outfit;

    /**
     * Create a Duck with the given id and name
     * 
     * @param id       The id of the Duck
     * @param name     The name of the Duck
     * @param quantity The number of ducks available
     * @param price    The price of the Duck
     * @param size     The size of the Duck
     * @param color    The color of the duck
     * @throws IllegalArgumentException If name is null, empty, or blank.
     *                                  If quanity is less than 0
     *                                  If price is less than 0
     *                                  If size/color/outfit is null
     *                                  If the duckOutfit is considered invalid
     * 
     * 
     *                                  {@literal @}JsonProperty is used in
     *                                  serialization and
     *                                  deserialization
     *                                  of the JSON object to the Java object in
     *                                  mapping the
     *                                  fields. If a field
     *                                  is not provided in the JSON object, the Java
     *                                  field gets
     *                                  the default Java
     *                                  value, i.e. 0 for int
     */
    public Duck(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("quantity") int quantity,
            @JsonProperty("price") double price, @JsonProperty("size") Size size, @JsonProperty("color") Colors color,
            @JsonProperty("outfit") DuckOutfit outfit) throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.color = color;
        this.outfit = outfit;

        String isValidResponse = isValid();
        if (isValidResponse != null) {
            throw new IllegalArgumentException(isValidResponse);
        }
    }

    /**
     * Retrieves the id of the Duck
     * 
     * @return The id of the Duck
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the name of the Duck - necessary for JSON object to Java object
     * deserialization
     * 
     * @param name The name of the Duck
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the Duck
     * 
     * @return The name of the Duck
     */
    public String getName() {
        return name;
    }

    /**
     * Get the quantity of the duck available
     * 
     * @return The quantity of the duck available
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the duck available
     * 
     * @param quantity The number of ducks available
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the price of the duck
     * 
     * @return The price of the duck
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the duck
     * 
     * @param price The price of the duck
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Sets the size of the Duck - necessary for JSON object to Java object
     * deserialization
     * 
     * @param size The size of the Duck
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Retrieves the size of the Duck
     * 
     * @return The size of the Duck
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the color of the Duck - necessary for JSON object to Java object
     * deserialization
     * 
     * @param color The color of the Duck.
     */
    public void setColor(Colors color) {
        this.color = color;
    }

    /**
     * Retrieves the color of the duck
     * 
     * @return The color of the duck.
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Sets a duck's outfit - necessary for JSON object to Java object
     * deserialization
     * 
     * @param outfit The new outfit
     */
    public void setOutfit(DuckOutfit outfit) {
        this.outfit = outfit;
    }

    /**
     * Gets a duck's outfit
     * 
     * @return The duck's outfit
     */
    public DuckOutfit getOutfit() {
        return this.outfit;
    }

    /**
     * Sets the Unique Identifier of the Hat the Duck is wearing
     * 
     * @param hatUID The Unique Identifier of the Hat the Duck is wearing.
     */
    public void setHatUID(int hatUID) {
        this.outfit.setHatUID(hatUID);
    }

    /**
     * Retrieves the Unique Identifier of the Hat the Duck is wearing.
     * 
     * @return The Unique Identifier of the Hat the Duck is wearing.
     */
    @Transient
    public int getHatUID() {
        return outfit.getHatUID();
    }

    /**
     * Sets the Unique Identifier of the Shirt the Duck is wearing
     * 
     * @param shirtUID The Unique Identifier of the Shirt the Duck is wearing.
     */
    public void setShirtUID(int shirtUID) {
        this.outfit.setShirtUID(shirtUID);
    }

    /**
     * Retrieves the Unique Identifier of the Shirt that the Duck is wearing
     * 
     * @return The Unique Identifier of the Shirt the Duck is wearing.
     */
    @Transient
    public int getShirtUID() {
        return this.outfit.getShirtUID();
    }

    /**
     * Sets the Unique Identifier of the Shoes the Duck is wearing
     * 
     * @param shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    public void setShoesUID(int shoesUID) {
        this.outfit.setShoesUID(shoesUID);
    }

    /**
     * Retrieves the Unique Identifier of the Shoes the Duck is wearing
     * 
     * @return shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    @Transient
    public int getShoesUID() {
        return this.outfit.getShoesUID();
    }

    /**
     * Sets the Unique Identifier of the item the Duck is holding in their hand
     * (feet?)
     * 
     * @param handItemUID The Unique Identifier of the item the Duck is holding in
     *                    their hand (feet?)
     */

    public void setHandItemUID(int handItemUID) {
        this.outfit.setHandItemUID(handItemUID);
    }

    /**
     * Retrieves the Unique Identifier of the item the Duck is holding in their hand
     * (feet?)
     * 
     * @return handItemUID The Unique Identifier of the item the Duck is holding in
     *         their hand (feet?)
     */
    @Transient
    public int getHandItemUID() {
        return this.outfit.getHandItemUID();
    }

    /**
     * Sets the Unique Identifier of the jewelry the Duck is wearing
     * 
     * @param jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    public void setJewelryUID(int jewelryUID) {
        this.outfit.setJewelryUID(jewelryUID);
    }

    /**
     * Retrieves the Unique Identifier of the jewelry the Duck is wearing
     * 
     * @return jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    @Transient
    public int getJewelryUID() {
        return this.outfit.getJewelryUID();
    }

    /**
     * Checks whether all of the properties are valid.
     * The class is considered invalid if any of the conditions are met:
     * 
     * If name is null, empty, or blank.
     * If quanity is less than 0
     * If price is less than 0
     * If size/color/outfit is null
     * If the duckOutfit is considered invalid
     * 
     * @return A string detailing what properties are invalid
     */
    private String isValid() {
        String issues = "";
        // Empty is considered blank as per java
        if (name == null || name.isBlank()) {
            issues += "Name must not be null, empty, or blank. ";
        }

        if (quantity < 0) {
            issues += "Quantity must be equal to or greater than 0. ";
        }

        if (price < 0) {
            issues += "Price must be greater than 0. ";
        }

        if (size == null) {
            issues += "Size must not be null. ";
        }

        if (color == null) {
            issues += "Color must not be null. ";
        }

        // Technically outfit should always be valid, but I will keep that check here
        // anyway
        if (outfit == null || outfit.isValid() != null) {
            issues += "DuckOutfit must not be null and must be valid. ";
        }

        return issues.isEmpty() ? null : issues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Duck)) {
            return false;
        }

        Duck other = (Duck) obj;
        return this.id == other.id && this.name.equals(other.name) && this.quantity == other.quantity
                && this.price == other.price && this.size == other.size && this.color == other.color
                && this.outfit.equals(other.outfit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name, quantity, price, size, color, outfit.getHatUID(),
                outfit.getShirtUID(), outfit.getShoesUID(), outfit.getHandItemUID(), outfit.getJewelryUID());
    }
}