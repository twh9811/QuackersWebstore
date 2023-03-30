package com.ducks.api.ducksapi.model;

/**
 * Represents Sizes of ducks that can be used.
 * @Author Travis Hill
 */
public enum Size {
    /**
     * Small Sized Duck
     */
    SMALL(3.00),
    /**
     * Medium Sized Duck
     */
    MEDIUM(5.00),
    /**
     * Large Sized Duck
     */
    LARGE(6.00),
    /**
     * Extra Large Sized Duck
     */
    EXTRA_LARGE(7.00);

    
    private double price;

    Size(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
