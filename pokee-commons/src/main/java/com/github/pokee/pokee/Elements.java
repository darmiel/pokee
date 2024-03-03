package com.github.pokee.pokee;

/**
 * The Elements enum represents the different elements that a breed can have.
 * Each element has a multiplier for the effectiveness of an attack against a defending element.
 */
public enum Elements {

    FIRE,
    GRASS,
    GROUND,
    ELECTRIC,
    WATER;

    public static final float EFFECTIVE_MULTIPLIER = 1.3f;
    public static final float INEFFECTIVE_MULTIPLIER = 0.7f;

    /**
     * Returns the multiplier for the effectiveness of an attack against a defending element.
     *
     * @param defender the defending element
     * @return the multiplier
     */
    public float getMultiplierAgainst(final Elements defender) {
        final int currentIndex = this.ordinal();
        final int defenderIndex = defender.ordinal();

        final int difference = currentIndex - defenderIndex;
        final int elementCountExclZero = Elements.values().length - 1;

        // if the difference is 1 or -`4`, the target element comes before the attacking element
        if (difference == 1 || difference == -elementCountExclZero) {
            return Elements.INEFFECTIVE_MULTIPLIER;
        }

        // if the difference is -1 or `4`, the target element comes after the attacking element
        if (difference == -1 || difference == elementCountExclZero) {
            return Elements.EFFECTIVE_MULTIPLIER;
        }

        // if the difference is something else the attack has a normal effectiveness
        return 1.0f;
    }

}
