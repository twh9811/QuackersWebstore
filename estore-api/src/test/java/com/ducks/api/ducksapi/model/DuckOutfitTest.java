package com.ducks.api.ducksapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class DuckOutfitTest {
    @Test
    public void testDuckOutfitEquals() throws IllegalArgumentException, IllegalAccessException {
        DuckOutfit expected = new DuckOutfit(0, 0, 0, 0, 0);

        // Test Equal
        assertEquals(expected, new DuckOutfit(0, 0, 0, 0, 0));

        // Test Not Equal (DuckOutfit)
        assertNotEquals(expected, new DuckOutfit(1, 0, 0, 0, 0));
        assertNotEquals(expected, new DuckOutfit(0, 2, 0, 0, 0));
        assertNotEquals(expected, new DuckOutfit(0, 0, 3, 0, 0));
        assertNotEquals(expected, new DuckOutfit(0, 0, 0, 4, 0));
        assertNotEquals(expected, new DuckOutfit(0, 0, 0, 0, 5));

        // Test Not Equal (Object)
        assertNotEquals(expected, new Object());
    }

    @Test
    public void testDuckOutfitInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new DuckOutfit(-1, -1, -1, -1, -1));
    }

    @Test
    public void testDuckOutfitToString() {
        DuckOutfit outfit = new DuckOutfit(1, 2, 3, 4, 5);
        // Setup
        String expected_string = String.format(DuckOutfit.STRING_FORMAT, outfit.getHatUID(), outfit.getShirtUID(),
                outfit.getShoesUID(), outfit.getHandItemUID(), outfit.getJewelryUID());

        // InvokDe
        String actual_string = outfit.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
}
