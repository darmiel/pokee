package com.github.pokee.pokee;

/**
 * The Upgrades enum represents the different upgrades that can be bought in the game.
 * Each upgrade has a name, a price, a multiplier per level and a maximum level.
 */
@SuppressWarnings("unused")
public enum Upgrades {

    HP("HP Upgrade", 100, 1.5f, 10),
    DEFENSE("Defense Upgrade", 100, 1.3f, 10),
    ATTACK("Attack Upgrade", 100, 1.3f, 10),
    SPEED("Spped Upgrade", 200, 1.5f, 10),
    MORE_ATTACK("Attack Upgrade", 100, 1.3f, 10),
    DOUBLE_JUMP("Double Jump", 3000, 1.0f, 1),
    NEW_ATTACK("New Attack", 100, 1.0f, 40);


    private final String name;
    private final long price;
    private final float multiplierPerLevel;
    private final long maxLevel;

    Upgrades(
            final String name,
            final long price, final
            float multiplierPerLevel,
            final long maxLevel
    ) {
        this.name = name;
        this.price = price;
        this.multiplierPerLevel = multiplierPerLevel;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public float getMultiplierPerLevel() {
        return multiplierPerLevel;
    }

    public long getMaxLevel() {
        return maxLevel;
    }

}
