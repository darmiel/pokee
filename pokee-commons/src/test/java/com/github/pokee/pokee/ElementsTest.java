package com.github.pokee.pokee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementsTest {

    @Test
    public void getMultiplierAgainst() {
        assertEquals(Elements.EFFECTIVE_MULTIPLIER, Elements.FIRE.getMultiplierAgainst(Elements.GRASS), 0.0f);
        assertEquals(Elements.EFFECTIVE_MULTIPLIER, Elements.GRASS.getMultiplierAgainst(Elements.GROUND), 0.0f);
        assertEquals(Elements.EFFECTIVE_MULTIPLIER, Elements.GROUND.getMultiplierAgainst(Elements.ELECTRIC), 0.0f);
        assertEquals(Elements.EFFECTIVE_MULTIPLIER, Elements.ELECTRIC.getMultiplierAgainst(Elements.WATER), 0.0f);
        assertEquals(Elements.EFFECTIVE_MULTIPLIER, Elements.WATER.getMultiplierAgainst(Elements.FIRE), 0.0f);

        assertEquals(Elements.INEFFECTIVE_MULTIPLIER, Elements.FIRE.getMultiplierAgainst(Elements.WATER), 0.0f);
        assertEquals(Elements.INEFFECTIVE_MULTIPLIER, Elements.GRASS.getMultiplierAgainst(Elements.FIRE), 0.0f);
        assertEquals(Elements.INEFFECTIVE_MULTIPLIER, Elements.GROUND.getMultiplierAgainst(Elements.GRASS), 0.0f);
        assertEquals(Elements.INEFFECTIVE_MULTIPLIER, Elements.ELECTRIC.getMultiplierAgainst(Elements.GROUND), 0.0f);
        assertEquals(Elements.INEFFECTIVE_MULTIPLIER, Elements.WATER.getMultiplierAgainst(Elements.ELECTRIC), 0.0f);

        for (final Elements value : Elements.values()) {
            assertEquals(1.0f, value.getMultiplierAgainst(value), 0.0f);
        }
    }

}