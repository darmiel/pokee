package com.github.pokee.pokee;

/**
 * The Breeds enum represents the different breeds that can be found in the game.
 * Each breed has a name, a type, a description, base stats and a maximum level.
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public enum Breeds {

    // Fire Buds
    MAGMITE(
            Elements.FIRE,
            "Magmite",
            "A molten creature emitting intense heat, found near volcanic vents.",
            33, 11, 7, 11
    ),
    CRIMSON(
            Elements.FIRE,
            "Crimson",
            "A tiny bird with crimson feathers, emitting heat from its tiny body.",
            32, 10, 6, 11
    ),
    PEBBLE(
            Elements.FIRE,
            "Pebble",
            "A small pebble infused with fiery energy, glowing red-hot.",
            34, 10, 8, 12
    ),

    // Water Buds
    FROSTLING(
            Elements.WATER,
            "Frostling",
            "A small icy creature with a chilly demeanor, frolicking in snowy landscapes.",
            28, 6, 8, 9
    ),
    WAVE(
            Elements.WATER,
            "Wave",
            "A creature crackling with energy, harnessing the power of ocean tides.",
            34, 11, 7, 12
    ),
    AQUAZORRA(
            Elements.WATER,
            "Aquazorra",
            "A tiny seedling with leaves resembling water lilies, thriving in wetlands.",
            34, 8, 10, 12
    ),

    // Grass Buds
    WHISK(
            Elements.GRASS,
            "Whisk",
            "A whiskered creature with aerodynamic features, riding air currents effortlessly.",
            28, 10, 5, 10
    ),
    FOOLY(
            Elements.GRASS,
            "Fooly",
            "A nimble ferret adorned with lush foliage, darting through dense underbrush.",
            35, 8, 11, 13
    ),
    VIGOR(
            Elements.GRASS,
            "Vigor",
            "A creature empowered by the vitality of lush vines, thriving in verdant forests.",
            36, 10, 11, 13
    ),

    // Ground Buds
    CLAYLASH(
            Elements.GROUND,
            "Claylash",
            "A sparkly creature composed of glittering stones, blending in with rocky landscapes.",
            37, 11, 9, 13
    ),
    TECTONIX(
            Elements.GROUND,
            "Tectonix",
            "A sturdy creature with a marble-like hide, blending into rocky terrain effortlessly.",
            39, 12, 10, 14
    ),
    QUAKE(
            Elements.GROUND,
            "Quake",
            "A cub with fur as dark as night, often seen prowling during dusk.",
            34, 9, 8, 12
    ),

    // Electric Buds
    SPARK(
            Elements.ELECTRIC,
            "Spark",
            "A creature with a shell crackling with electricity, emitting sparks when threatened.",
            31, 10, 9, 11
    ),
    JOLT(
            Elements.ELECTRIC,
            "Jolt",
            "A pulsating creature emitting electric shocks, capable of stunning foes with its charged attacks.",
            32, 14, 8, 11
    ),
    ZAP(
            Elements.ELECTRIC,
            "Zap",
            "A tadpole crackling with electric energy, its movements generating sparks.",
            30, 10, 6, 10
    );

    private final String name;
    private final Elements type;
    private final String description;
    private final int baseHP;
    private final int baseAttack;
    private final int baseDefense;
    private final int maxLevel;

    Breeds(
            final Elements type,
            final String name,
            final String description,
            final int baseHP,
            final int baseAttack,
            final int baseDefense,
            final int maxLevel

    ) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.baseHP = baseHP;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.maxLevel = maxLevel;
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

    public int getBaseHP() {
        return baseHP;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

}
