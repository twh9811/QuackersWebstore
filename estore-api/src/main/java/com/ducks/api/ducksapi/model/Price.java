package com.ducks.api.ducksapi.model;

public class Price {
    private double finalPrice;
    private double basePrice;
    private double outfitPrice = 2.00;


    public Price(Duck duck) {
        this.finalPrice = calculatePrice(duck);
    }

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

    public double getPrice() {
        return this.finalPrice;
    }
}
