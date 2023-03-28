package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for custom prices
 * 
 * @author Travis Hill
 */
@Tag("Model-tier")
public class PriceTest {
    private Duck testDuck;

    @BeforeEach
    public void setupDuckTest() {
        int expected_id = 99;
        int expected_quantity = 10;
        double default_price = 0;
        String expected_name = "Wi-Fire";
        Colors expected_color = Colors.BLUE;
        Size expected_size = Size.SMALL;
        int expected_hat_uid = 0;
        int expected_shirt_uid = 0;
        int expected_shoes_uid = 0;
        int expected_handitem_uid = 0;
        int expected_jewelry_uid = 0;

        DuckOutfit outfitOne = new DuckOutfit(expected_hat_uid, expected_shirt_uid, expected_shoes_uid,
                expected_handitem_uid, expected_jewelry_uid);

        // Invoke
        testDuck = new Duck(expected_id, expected_name, expected_quantity, default_price, expected_size,
                expected_color, outfitOne);

    }

    @Test
    public void duckNoOutfitTest() {
        double expectedSmall = 3.00 + 1.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void duckOneCustomOutfit() {
        testDuck.setHatUID(1);
        double expectedSmall = 3.00 + 1.00 + 2.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00 + 2.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00 + 2.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00 + 2.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void duckTwoCustomOutfit() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        double expectedSmall = 3.00 + 1.00 + 4.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00 + 4.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00 + 4.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00 + 4.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void duckThreeCustomOutfit() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        testDuck.setShoesUID(1);
        double expectedSmall = 3.00 + 1.00 + 6.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00 + 6.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00 + 6.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00 + 6.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void duckFourCustomOutfit() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        testDuck.setShoesUID(1);
        testDuck.setHandItemUID(1);
        double expectedSmall = 3.00 + 1.00 + 8.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00 + 8.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00 + 8.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00 + 8.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void duckFullCustomOutfit() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        testDuck.setShoesUID(1);
        testDuck.setHandItemUID(1);
        testDuck.setJewelryUID(1);
        double expectedSmall = 3.00 + 1.00 + 10.00;
        assertEquals(expectedSmall, testDuck.getPrice());

        Size medium = Size.MEDIUM;
        testDuck.setSize(medium);
        double expectedMedium = 5.00 + 1.00 + 10.00;
        assertEquals(expectedMedium, testDuck.getPrice());

        Size large = Size.LARGE;
        testDuck.setSize(large);
        double expectedLarge = 6.00 + 1.00 + 10.00;
        assertEquals(expectedLarge, testDuck.getPrice());

        Size extraLarge = Size.EXTRA_LARGE;
        testDuck.setSize(extraLarge);
        double expectedExtraLarge = 7.00 + 1.00 + 10.00;
        assertEquals(expectedExtraLarge, testDuck.getPrice());
    }

    @Test
    public void customPriceTest() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        testDuck.setShoesUID(1);
        testDuck.setHandItemUID(1);
        testDuck.setJewelryUID(1);
        testDuck.setPrice(19.99);

        double expectedPrice = 19.99;

        assertEquals(expectedPrice, testDuck.getPrice());
    }

    @Test
    public void defaultPriceTest() {
        testDuck.setHatUID(1);
        testDuck.setShirtUID(1);
        testDuck.setShoesUID(1);
        testDuck.setHandItemUID(1);
        testDuck.setJewelryUID(1);
        testDuck.setPrice(0.0);

        double expectedPrice = 3.00 + 1.00 + 10.00;
        assertEquals(expectedPrice, testDuck.getPrice());
    }
}
