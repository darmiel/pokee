package com.github.pokee.commons;

public enum AttackType {


    FIRESTORM(ElementType.FIRE, "Fire Storm", "A storm of fire", 5, 130, 0.5f),
    AQUAJET(ElementType.WATER, "Aqua Jet", "A high-speed jet of water", 10, 100, 0.8f),
    LEAFBLADE(ElementType.GRASS, "Leaf Blade", "A sharp blade of leaves", 8, 120, 0.9f),
    EARTHQUAKE(ElementType.GROUND, "Earthquake", "A powerful quake shaking the ground", 5, 140, 0.7f),
    THUNDERBOLT(ElementType.ELECTRIC, "Thunderbolt", "A bolt of lightning", 12, 110, 0.85f),
    FLAMETHROWER(ElementType.FIRE, "Flamethrower", "A stream of flames", 10, 120, 0.8f),
    HYDROPUMP(ElementType.WATER, "Hydro Pump", "A powerful blast of water", 7, 150, 0.75f),
    SOLARBEAM(ElementType.GRASS, "Solar Beam", "A beam of solar energy", 5, 140, 0.9f),
    MAGNITUDE(ElementType.GROUND, "Magnitude", "Varies in magnitude, causing earthquakes", 10, 100, 0.7f),
    THUNDER(ElementType.ELECTRIC, "Thunder", "A loud thunderclap with lightning", 8, 130, 0.7f),
    BLAZEKICK(ElementType.FIRE, "Blaze Kick", "A flaming kick", 12, 110, 0.85f),
    SURF(ElementType.WATER, "Surf", "A tidal wave", 10, 120, 0.85f),
    ENERGYBALL(ElementType.GRASS, "Energy Ball", "A ball of energy", 12, 110, 0.85f),
    SANDSTORM(ElementType.GROUND, "Sandstorm", "A storm of swirling sand", 5, 90, 0.95f),
    VOLTCHANGE(ElementType.ELECTRIC, "Volt Change", "A sudden change in voltage", 8, 100, 0.9f),
    FLAREBLITZ(ElementType.FIRE, "Flare Blitz", "A reckless charge engulfed in flames", 10, 130, 0.8f),
    AQUARING(ElementType.WATER, "Aqua Ring", "A ring of healing water", 15, 0, 1.0f),
    SEEDBOMB(ElementType.GRASS, "Seed Bomb", "Explosive seeds", 10, 120, 0.9f),
    MUDSLAP(ElementType.GROUND, "Mud Slap", "A slinging of mud", 15, 80, 0.95f),
    THUNDERSHOCK(ElementType.ELECTRIC, "Thunder Shock", "A jolt of electricity", 20, 70, 0.9f);

    private final ElementType type;
    private final String name;
    private final String description;
    private final long ppCost;
    private final long strength;
    private final float accuracy;

    AttackType(ElementType type, String name, String description, long ppCost, long strength, float accuracy) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.ppCost = ppCost;
        this.strength = strength;
        this.accuracy = accuracy;
    }

}
