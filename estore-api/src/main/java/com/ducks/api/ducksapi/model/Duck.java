package com.ducks.api.ducksapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Duck entity
 * 
 * @author SWEN Faculty
 */
public class Duck {
    private static final Logger LOG = Logger.getLogger(Duck.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Duck [id=%d, name=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("size") private Size size;
    @JsonProperty("color") private Colors color;
    @JsonProperty("hatUID") private int hatUID;
    @JsonProperty("shirtUID") private int shirtUID;
    @JsonProperty("shoesUID") private int shoesUID;
    @JsonProperty("handItemUID") private int handItemUID;
    @JsonProperty("jewelryUID") private int jewelryUID;

    


    /**
     * Create a Duck with the given id and name
     * @param id The id of the Duck
     * @param name The name of the Duck
     * @param size The size of the Duck
     * @param color The color of the duck
     * @param hatUID The ID of the hat the Duck is wearing
     * @param shirtUID The ID of the shirt the Duck is wearing
     * @param shoesUID The ID of the shoes the Duck is wearing
     * @param handItemUID The ID of the item the Duck is holding in their hand (feet?)
     * @param jewelryUID The ID of the jewerly the Duck is wearing  
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Duck(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("size") Size size, @JsonProperty("color") Colors color, @JsonProperty("hatUID") int hatUID,
        @JsonProperty("shirtUID") int shirtUID, @JsonProperty("shoesUID") int shoesUID, @JsonProperty("handItemUID") int handItemUID, @JsonProperty("jewerlyUID") int jewelryUID) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.color = color;
        this.hatUID = hatUID;
        this.shirtUID = shirtUID;
        this.shoesUID = shoesUID;
        this.handItemUID = handItemUID;
        this.jewelryUID = jewelryUID;
    }

    /**
     * Retrieves the id of the Duck
     * @return The id of the Duck
     */
    public int getId() {return id;}

    /**
     * Sets the name of the Duck - necessary for JSON object to Java object deserialization
     * @param name The name of the Duck
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the Duck
     * @return The name of the Duck
     */
    public String getName() {return name;}

    /**
     * Sets the size of the Duck - necessary for JSON object to Java object deserialization
     * @param size The size of the Duck
     */
    public void setSize(Size size) {this.size = size;}

    /**
     * Retrieves the size of the Duck
     * @return The size of the Duck
     */
    public Size getSize() {return size;}

    /**
     * Sets the color of the Duck - necessary for JSON object to Java object deserialization
     * @param color The color of the Duck.
     */
    public void setColor(Colors color) {this.color = color;}

    /**
     * Retrieves the color of the duck
     * @return The color of the duck.
     */
    public Colors getColor() { return color;}

    /**
     * Sets the Unique Identifier of the Hat the Duck is wearing - necessary for JSON object to Java object deserialization
     * @param hatUID The Unique Identifier of the Hat the Duck is wearing.
     */
    public void setHatUID(int hatUID) {this.hatUID = hatUID;}

    /**
     * Retrieves the Unique Identifier of the Hat the Duck is wearing.
     * @return The Unique Identifier of the Hat the Duck is wearing.
     */
    public int getHatUID() {return hatUID;}

    /**
     * Sets the Unique Identifier of the Shirt the Duck is wearing - necessary for JSON object to Java object deserialization
     * @param shirtUID The Unique Identifier of the Shirt the Duck is wearing.
     */
    public void setShirtUID(int shirtUID) {this.shirtUID = shirtUID;}

    /**
     * Retrieves the Unique Identifier of the Shirt that the Duck is wearing
     * @return The Unique Identifier of the Shirt the Duck is wearing. 
     */
    public int getShirtUID() {return shirtUID;}

    /**
     * Sets the Unique Identifier of the Shoes the Duck is wearing - necessary for JSON object to Java object deserialization
     * @param shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    public void setShoesUID(int shoesUID) {this.shoesUID = shoesUID;}
    
    /**
     * Retrieves the Unique Identifier of the Shoes the Duck is wearing - necessary for JSON object to Java object deserialization
     * @return shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    public int getShoesUID() {return shoesUID;}
    
    /**
     * Sets the Unique Identifier of the item the Duck is holding in their hand (feet?) - necessary for JSON object to Java object deserialization
     * @param handItemUID The Unique Identifier of the item the Duck is holding in their hand (feet?)
     */
    public void setHandItemUID(int handItemUID) {this.handItemUID = handItemUID;}

    /**
     * Retrieves the Unique Identifier of the item the Duck is holding in their hand (feet?)
     * @return handItemUID The Unique Identifier of the item the Duck is holding in their hand (feet?)
     */
    public int getHandItemUID() {return handItemUID;}

    /**
     * Sets the Unique Identifier of the jewelry the Duck is wearing - necessary for JSON object to Java object deserialization
     * @param jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    public void setJewelryUID(int jewelryUID) { this.jewelryUID = jewelryUID;}

    /**
     * Retrieves the Unique Identifier of the jewelry the Duck is wearing
     * @return jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    public int getJewelryUID() {return jewelryUID;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name);
    }
}