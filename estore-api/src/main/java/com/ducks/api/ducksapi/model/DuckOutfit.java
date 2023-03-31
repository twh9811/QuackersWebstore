package com.ducks.api.ducksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DuckOutfit {

    static final String STRING_FORMAT = "DuckOutfit [hatUID=%d, shirtUID=%d, shoesUID=%d, handitemUID=%d, jewelryUID=%d]";

    @JsonProperty("hatUID")
    private int hatUID;

    @JsonProperty("shirtUID")
    private int shirtUID;

    @JsonProperty("shoesUID")
    private int shoesUID;

    @JsonProperty("handItemUID")
    private int handItemUID;

    @JsonProperty("jewelryUID")
    private int jewelryUID;

    /**
     * Creates a DuckOutfit with the given properties
     * 
     * @param hatUID      The ID of the hat the Duck is wearing
     * @param shirtUID    The ID of the shirt the Duck is wearing
     * @param shoesUID    The ID of the shoes the Duck is wearing
     * @param handItemUID The ID of the item the Duck is holding in their hand
     *                    (feet?)
     * @param jewelryUID  The ID of the jewerly the Duck is wearing
     * 
     * @throws IllegalArgumentException If any of the passed parameters are less
     *                                  than 0
     */
    public DuckOutfit(@JsonProperty("hatUID") int hatUID,
            @JsonProperty("shirtUID") int shirtUID, @JsonProperty("shoesUID") int shoesUID,
            @JsonProperty("handItemUID") int handItemUID, @JsonProperty("jewelryUID") int jewelryUID)
            throws IllegalArgumentException {
        this.hatUID = hatUID;
        this.shirtUID = shirtUID;
        this.shoesUID = shoesUID;
        this.handItemUID = handItemUID;
        this.jewelryUID = jewelryUID;

        String isValidResponse = isValid();
        if (isValidResponse != null) {
            throw new IllegalArgumentException(isValidResponse);
        }
    }

    /**
     * Sets the Unique Identifier of the Hat the Duck is wearing - necessary for
     * JSON object to Java object deserialization
     * 
     * @param hatUID The Unique Identifier of the Hat the Duck is wearing.
     */
    public void setHatUID(int hatUID) {
        this.hatUID = hatUID;
    }

    /**
     * Retrieves the Unique Identifier of the Hat the Duck is wearing.
     * 
     * @return The Unique Identifier of the Hat the Duck is wearing.
     */
    public int getHatUID() {
        return hatUID;
    }

    /**
     * Sets the Unique Identifier of the Shirt the Duck is wearing - necessary for
     * JSON object to Java object deserialization
     * 
     * @param shirtUID The Unique Identifier of the Shirt the Duck is wearing.
     */
    public void setShirtUID(int shirtUID) {
        this.shirtUID = shirtUID;
    }

    /**
     * Retrieves the Unique Identifier of the Shirt that the Duck is wearing
     * 
     * @return The Unique Identifier of the Shirt the Duck is wearing.
     */
    public int getShirtUID() {
        return shirtUID;
    }

    /**
     * Sets the Unique Identifier of the Shoes the Duck is wearing - necessary for
     * JSON object to Java object deserialization
     * 
     * @param shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    public void setShoesUID(int shoesUID) {
        this.shoesUID = shoesUID;
    }

    /**
     * Retrieves the Unique Identifier of the Shoes the Duck is wearing - necessary
     * for JSON object to Java object deserialization
     * 
     * @return shoesUID The Unique Identifier of the Shoes the Duck is wearing.
     */
    public int getShoesUID() {
        return shoesUID;
    }

    /**
     * Sets the Unique Identifier of the item the Duck is holding in their hand
     * (feet?) - necessary for JSON object to Java object deserialization
     * 
     * @param handItemUID The Unique Identifier of the item the Duck is holding in
     *                    their hand (feet?)
     */
    public void setHandItemUID(int handItemUID) {
        this.handItemUID = handItemUID;
    }

    /**
     * Retrieves the Unique Identifier of the item the Duck is holding in their hand
     * (feet?)
     * 
     * @return handItemUID The Unique Identifier of the item the Duck is holding in
     *         their hand (feet?)
     */
    public int getHandItemUID() {
        return handItemUID;
    }

    /**
     * Sets the Unique Identifier of the jewelry the Duck is wearing - necessary for
     * JSON object to Java object deserialization
     * 
     * @param jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    public void setJewelryUID(int jewelryUID) {
        this.jewelryUID = jewelryUID;
    }

    /**
     * Retrieves the Unique Identifier of the jewelry the Duck is wearing
     * 
     * @return jewelryUID The Unique Identifier of the jewelry the Duck is wearing
     */
    public int getJewelryUID() {
        return jewelryUID;
    }

    public int[] getOutfitAsArray() {
        return new int[] { hatUID, shirtUID, shoesUID, handItemUID, jewelryUID };
    }

    /**
     * Checks whether all of the properties are valid.
     * The class is considered invalid if any of the properties are less than 0
     * 
     * @return A string detailing what properties are invalid
     */
    protected String isValid() {
        String issues = "";
        if (hatUID < 0) {
            issues += "hatUID must be equal to or greater than 0. ";
        }

        if (shirtUID < 0) {
            issues += "shirtUID must be equal to or greater than 0. ";
        }

        if (shoesUID < 0) {
            issues += "shoesUID must be equal to or greater than 0. ";
        }

        if (handItemUID < 0) {
            issues += "handItemUID must be equal to or greater than 0. ";
        }

        if (jewelryUID < 0) {
            issues += "jewelryUID must be equal to or greater than 0. ";
        }

        return issues.isEmpty() ? null : issues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DuckOutfit)) {
            return false;
        }

        DuckOutfit other = (DuckOutfit) obj;
        return this.hatUID == other.hatUID && this.shirtUID == other.shirtUID && this.shoesUID == other.shoesUID
                && this.handItemUID == other.handItemUID
                && this.jewelryUID == other.jewelryUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.hatUID + this.shirtUID + this.shoesUID + this.handItemUID + this.jewelryUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, hatUID, shirtUID, shoesUID, handItemUID, jewelryUID);
    }
}
