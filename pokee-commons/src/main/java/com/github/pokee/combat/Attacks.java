package com.github.pokee.combat;

import com.github.pokee.pokee.Elements;

/**
 * The Attacks enum represents the different attacks that can be used in the game.
 * Each attack has a type, a name, a description, a cost in PP, a strength and an accuracy.
 */
@SuppressWarnings("unused")
public enum Attacks {

    // Fire Attacks
    FIRESTORM(
            Elements.FIRE,
            "Fire Storm",
            "A storm of fire",
            5, 130, 0.5f
    ),
    FLAMETHROWER(
            Elements.FIRE,
            "Flamethrower",
            "A stream of flames",
            10, 120, 0.8f
    ),
    BLAZEKICK(Elements.FIRE,
            "Blaze Kick",
            "A flaming kick",
            12, 110, 0.85f
    ),
    FLAREBLITZ(Elements.FIRE,
            "Flare Blitz",
            "A reckless charge engulfed in flames",
            10, 130, 0.8f
    ),


    // Water Attacks
    AQUAJET(
            Elements.WATER,
            "Aqua Jet",
            "A high-speed jet of water",
            10, 100, 0.8f
    ),
    HYDROPUMP(
            Elements.WATER,
            "Hydro Pump",
            "A powerful blast of water",
            7, 150, 0.75f
    ),
    SURF(
            Elements.WATER,
            "Surf",
            "A tidal wave",
            10, 120, 0.85f
    ),
    AQUARING(
            Elements.WATER,
            "Aqua Ring",
            "A ring of healing water",
            15, 0, 1.0f
    ),


    // Grass Attacks
    LEAFBLADE(
            Elements.GRASS,
            "Leaf Blade",
            "A sharp blade of leaves",
            8, 120, 0.9f
    ),
    SOLARBEAM(
            Elements.GRASS,
            "Solar Beam",
            "A beam of solar energy",
            5, 140, 0.9f
    ),
    ENERGYBALL(
            Elements.GRASS,
            "Energy Ball",
            "A ball of energy",
            12, 110, 0.85f
    ),
    SEEDBOMB(
            Elements.GRASS,
            "Seed Bomb",
            "Explosive seeds",
            10, 120, 0.9f
    ),


    // Electric Attacks
    THUNDERBOLT(
            Elements.ELECTRIC,
            "Thunderbolt",
            "A bolt of lightning",
            12, 110, 0.85f
    ),
    THUNDER(
            Elements.ELECTRIC,
            "Thunder",
            "A loud thunderclap with lightning",
            8, 130, 0.7f
    ),
    VOLTCHANGE(
            Elements.ELECTRIC,
            "Volt Change",
            "A sudden change in voltage",
            8, 100, 0.9f
    ),
    THUNDERSHOCK(
            Elements.ELECTRIC,
            "Thunder Shock",
            "A jolt of electricity",
            20, 70, 0.9f
    ),


    // Ground Attacks
    EARTHQUAKE(
            Elements.GROUND,
            "Earthquake",
            "A powerful quake shaking the ground",
            5, 140, 0.7f
    ),
    MAGNITUDE(
            Elements.GROUND,
            "Magnitude",
            "Varies in magnitude causing earthquakes",
            10, 100, 0.7f
    ),
    SANDSTORM(
            Elements.GROUND,
            "Sandstorm",
            "A storm of swirling sand",
            5, 90, 0.95f
    ),
    MUDSLAP(
            Elements.GROUND,
            "Mud Slap",
            "A slinging of mud",
            15, 80, 0.95f
    );

    private final Elements type;
    private final String name;
    private final String description;
    private final long ppCost;
    private final long strength;
    private final float accuracy;

    /**
     * Creates a new attack with the given properties.
     *
     * @param type        the type of the attack
     * @param name        the name of the attack
     * @param description the description of the attack
     * @param ppCost      the cost in PP of the attack
     * @param strength    the strength of the attack
     * @param accuracy    the accuracy of the attack
     */
    Attacks(
            final Elements type,
            final String name,
            final String description,
            final long ppCost,
            final long strength,
            final float accuracy
    ) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.ppCost = ppCost;
        this.strength = strength;
        this.accuracy = accuracy;
    }

    public Elements getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getPpCost() {
        return ppCost;
    }

    public long getStrength() {
        return strength;
    }

    public float getAccuracy() {
        return accuracy;
    }

}
