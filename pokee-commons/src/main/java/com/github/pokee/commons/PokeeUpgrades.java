package com.github.pokee.commons;

public enum PokeeUpgrades {
    HP("HP Upgrade", 100, 1.5f, 10),
    DEFENSE("Defense Upgrade", 100, 1.3f, 10),
    ATTACK("Attack Upgrade", 100, 1.3f, 10),
    SPEED("Spped Upgrade", 200, 1.5f, 10),
    MORE_ATTACK("Attack Upgrade", 100, 1.3f, 10),
    DOUBLE_JUMP("Double Jump", 3000, 1.0f, 1),
    NEW_ATTACK("New Attack", 100, 1.0f, 40);


    private final String name;
    private  final long price;
    private final float multiplierperLevel;
    private final long maxLevel;

    PokeeUpgrades(String name, long price, float multiplierperLevel, long maxLevel) {
        this.name = name;
        this.price = price;
        this.multiplierperLevel = multiplierperLevel;
        this.maxLevel = maxLevel;
    }
}
