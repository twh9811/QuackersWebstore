package com.ducks.api.ducksapi.model;

public class Price {
    private double finalPrice;
    private double basePrice = 5;
    private double outfitPrice=  1;

    private DuckOutfit outfit;


    public Price(Duck duck) {
        this.finalPrice = calculatePrice(duck);
    }

    private double calculatePrice(Duck duck) {
        DuckOutfit outfit = duck.getOutfit();
        int[] outfitAsArray = outfit.getOutfitAsArray();
        for(int i=0; i<outfitAsArray.length; i++) {
            // No customized option.
            if(outfitAsArray[i] == 0) {
                continue;
            }
            basePrice += outfitPrice;
            System.out.println(basePrice);
        }
        Colors color = duck.getColor();
        Size size = duck.getSize();
        basePrice = basePrice + size.getPrice() + color.getPrice();
        return basePrice;
    }

    public double getPrice() {
        return this.finalPrice;
    }
}
