package com.ducks.api.ducksapi.model;
/**
 * Reperesnts the colors of a Duck that can be used.
 * @author Travis Hill
 */
public enum Colors {
    /**
     * Red Color
     */
    RED,
    /**
     * Orange Color
     */
    ORANGE,
    /**
     * Yellow Color
     */
    YELLOW,
    /**
     * Green Color
     */
    GREEN,
    /**
     * Blue Color
     */
    BLUE,
    /**
     * Indigo Color
     */
    INDIGO,
    /**
     * Violet Color
     */
    VIOLET;

    private double price;

    Colors() {
        this.price = 1.00;
    }

    public double getPrice() {
        return price;
    }
}
