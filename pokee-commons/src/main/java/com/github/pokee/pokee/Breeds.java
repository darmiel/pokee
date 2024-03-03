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
            33,
            11,
            7,
            11,
            "A molten creature emitting intense heat, found near volcanic vents."
    ),
    CRIMSON(
            Elements.FIRE,
            "Crimson",
            32,
            10,
            6,
            11,
            "A tiny bird with crimson feathers, emitting heat from its tiny body."
    ),
    MOTHLAVA(
            Elements.FIRE,
            "Mothlava",
            35,
            14,
            8,
            12,
            "A fiery moth with wings ablaze, fluttering around volcanic regions."
    ),
    PEBBLE(
            Elements.FIRE,
            "Pebble",
            34,
            10,
            8,
            12,
            "A small pebble infused with fiery energy, glowing red-hot."
    ),
    BEETLY(
            Elements.FIRE,
            "Beetly",
            37,
            12,
            8,
            13,
            "A beetle with a fiery carapace, emitting intense heat as it scuttles across the ground."
    ),
    FLUFF(
            Elements.FIRE,
            "Fluff",
            32,
            11,
            6,
            11,
            "A fluffy creature emitting warm, comforting heat from its fur."
    ),

    // Water Buds
    FROSTLING(
            Elements.WATER,
            "Frostling",
            28,
            6,
            8,
            9,
            "A small icy creature with a chilly demeanor, frolicking in snowy landscapes."
    ),
    SURF(
            Elements.WATER,
            "Surf",
            33,
            10,
            7,
            11,
            "A sleek aquatic creature that surfs swiftly along ocean currents."
    ),
    WAVE(
            Elements.WATER,
            "Wave",
            34,
            11,
            7,
            12,
            "A creature crackling with energy, harnessing the power of ocean tides."
    ),
    GLACIMAW(
            Elements.WATER,
            "Glacimaw",
            31,
            9,
            11,
            11,
            "A creature with icy fangs, capable of freezing its prey with a single bite."
    ),
    HYDRUFFIN(
            Elements.WATER,
            "Hydruffin",
            32,
            11,
            9,
            12,
            "A mysterious creature dwelling in marshlands, its body composed of shimmering water."
    ),
    AQUAZORRA(
            Elements.WATER,
            "Aquazorra",
            34,
            8,
            10,
            12,
            "A tiny seedling with leaves resembling water lilies, thriving in wetlands."
    ),

    // Grass Buds
    BRAMBLE(
            Elements.GRASS,
            "Bramble",
            36,
            9,
            10,
            13,
            "A spiky hedgehog covered in thorny brambles, defending its territory fiercely."
    ),
    WHISK(
            Elements.GRASS,
            "Whisk",
            28,
            10,
            5,
            10,
            "A whiskered creature with aerodynamic features, riding air currents effortlessly."
    ),
    FOOLY(
            Elements.GRASS,
            "Fooly",
            35,
            8,
            11,
            13,
            "A nimble ferret adorned with lush foliage, darting through dense underbrush."
    ),
    ZEPHYR(
            Elements.GRASS,
            "Zephyr",
            31,
            11,
            7,
            11,
            "A winged creature with feathers as light as the wind, soaring gracefully through the air."
    ),
    VIGOR(
            Elements.GRASS,
            "Vigor",
            36,
            10,
            11,
            13,
            "A creature empowered by the vitality of lush vines, thriving in verdant forests."
    ),
    CIRRUS(
            Elements.GRASS,
            "Cirrus",
            30,
            12,
            6,
            10,
            "A creature with razor-sharp claws and feathered wings, soaring among wispy clouds."
    ),

    // Ground Buds
    GRITFANG(
            Elements.GROUND,
            "Gritfang",
            38,
            10,
            12,
            14,
            "A sturdy hatchling with a rocky shell, camouflaged among mountainous terrain."
    ),
    CLAYLASH(
            Elements.GROUND,
            "Claylash",
            37,
            11,
            9,
            13,
            "A sparkly creature composed of glittering stones, blending in with rocky landscapes."
    ),
    TECTONIX(
            Elements.GROUND,
            "Tectonix",
            39,
            12,
            10,
            14,
            "A sturdy creature with a marble-like hide, blending into rocky terrain effortlessly."
    ),
    GRAVL(
            Elements.GROUND,
            "Gravl",
            38,
            12,
            9,
            13,
            "A swift colt bounding through rocky canyons with ease."
    ),
    QUAKE(
            Elements.GROUND,
            "Quake",
            34,
            9,
            8,
            12,
            "A cub with fur as dark as night, often seen prowling during dusk."
    ),
    STONEWING(
            Elements.GROUND,
            "Stonewing",
            36,
            11,
            10,
            13,
            "A creature with a rocky shell, its wings adorned with precious stones."
    ),

    // Electric Buds
    SPARK(
            Elements.ELECTRIC,
            "Spark",
            31,
            10,
            9,
            11,
            "A creature with a shell crackling with electricity, emitting sparks when threatened."
    ),
    BLITZ(
            Elements.ELECTRIC,
            "Blitz",
            31,
            13,
            7,
            10,
            "A small creature crackling with electric energy, zapping unsuspecting targets."
    ),
    JOLT(
            Elements.ELECTRIC,
            "Jolt",
            32,
            14,
            8,
            11,
            "A pulsating creature emitting electric shocks, capable of stunning foes with its charged attacks."
    ),
    CRACKLE(
            Elements.ELECTRIC,
            "Crackle",
            36,
            9,
            10,
            13,
            "A crab crackling with electric energy, its claws sparking with each pinch."
    ),
    ZAP(
            Elements.ELECTRIC,
            "Zap",
            30,
            10,
            6,
            10,
            "A tadpole crackling with electric energy, its movements generating sparks."
    ),
    ION(
            Elements.ELECTRIC,
            "Ion",
            30,
            12,
            7,
            11,
            "A small creature crackling with electric energy, its fur standing on end."
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
            final int baseHP,
            final int baseAttack,
            final int baseDefense,
            final int maxLevel,
            final String description

    ) {
        this.type = type;
        this.name = name;
        this.baseHP = baseHP;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.maxLevel = maxLevel;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Elements getType() {
        return type;
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
