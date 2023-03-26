package com.ducks.api.ducksapi.model;

import org.springframework.stereotype.Component;

/**
 * Represents the price of duck.
 * 
 * @author Travis Hill
 */
@Component
public class Price {
    private double finalPrice;
    private double basePrice;
    private double outfitPrice = 2.00;
    
    /*
     * Creates a Price object that stores the price of a duck
     * 
     * @param duck The duck object the price is being made for.
     */
    public Price(Duck duck) {
        this.finalPrice = calculatePrice(duck);
    }

    /**
     * Calculates the price of a duck based on its size, color, and outfit attributes.
     * 
     * @param duck The duck that the price is being based off of.
     * @return The total price of the duck as a double.
     */
    private double calculatePrice(Duck duck) {
        DuckOutfit outfit = duck.getOutfit();
        Colors color = duck.getColor();
        Size size = duck.getSize();
        basePrice = color.getPrice() + size.getPrice();
        int[] outfitAsArray = outfit.getOutfitAsArray();
        for(int i=0; i<outfitAsArray.length; i++) {
            // No customized option.
            if(outfitAsArray[i] == 0) {
                continue;
            }
            basePrice += outfitPrice;
            System.out.println(basePrice);
        }
        return this.basePrice;
    }

    /*
     * Returns the price of the price object.
     */
    public double getPrice() {
        return this.finalPrice;
    }
}
