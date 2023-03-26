package com.ducks.api.ducksapi.model;

import java.lang.reflect.Field;

public class Price {
    private double price;
    private Duck duck;
    private DuckOutfit outfit;

    public Price(Duck duck) {
        this.duck = duck;
        this.price = calculatePrice();
    }

    private double calculatePrice() {
        DuckOutfit outfit = duck.getOutfit();
        Field[] items = outfit
    }
}
