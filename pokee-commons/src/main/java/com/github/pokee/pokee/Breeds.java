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
    MOTHLAVA(
            Elements.FIRE,
            "Mothlava",
            "A fiery moth with wings ablaze, fluttering around volcanic regions.",
            35, 14, 8, 12
    ),
    PEBBLE(
            Elements.FIRE,
            "Pebble",
            "A small pebble infused with fiery energy, glowing red-hot.",
            34, 10, 8, 12
    ),
    BEETLY(
            Elements.FIRE,
            "Beetly",
            "A beetle with a fiery carapace, emitting intense heat as it scuttles across the ground.",
            37, 12, 8, 13
    ),
    FLUFF(
            Elements.FIRE,
            "Fluff",
            "A fluffy creature emitting warm, comforting heat from its fur.",
            32, 11, 6, 11
    ),

    // Water Buds
    FROSTLING(
            Elements.WATER,
            "Frostling",
            "A small icy creature with a chilly demeanor, frolicking in snowy landscapes.",
            28, 6, 8, 9
    ),
    SURF(
            Elements.WATER,
            "Surf",
            "A sleek aquatic creature that surfs swiftly along ocean currents.",
            33, 10, 7, 11
    ),
    WAVE(
            Elements.WATER,
            "Wave",
            "A creature crackling with energy, harnessing the power of ocean tides.",
            34, 11, 7, 12
    ),
    GLACIMAW(
            Elements.WATER,
            "Glacimaw",
            "A creature with icy fangs, capable of freezing its prey with a single bite.",
            31, 9, 11, 11
    ),
    HYDRUFFIN(
            Elements.WATER,
            "Hydruffin",
            "A mysterious creature dwelling in marshlands, its body composed of shimmering water.",
            32, 11, 9, 12
    ),
    AQUAZORRA(
            Elements.WATER,
            "Aquazorra",
            "A tiny seedling with leaves resembling water lilies, thriving in wetlands.",
            34, 8, 10, 12
    ),

    // Grass Buds
    BRAMBLE(
            Elements.GRASS,
            "Bramble",
            "A spiky hedgehog covered in thorny brambles, defending its territory fiercely.",
            36, 9, 10, 13
    ),
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
    ZEPHYR(
            Elements.GRASS,
            "Zephyr",
            "A winged creature with feathers as light as the wind, soaring gracefully through the air.",
            31, 11, 7, 11
    ),
    VIGOR(
            Elements.GRASS,
            "Vigor",
            "A creature empowered by the vitality of lush vines, thriving in verdant forests.",
            36, 10, 11, 13
    ),
    CIRRUS(
            Elements.GRASS,
            "Cirrus",
            "A creature with razor-sharp claws and feathered wings, soaring among wispy clouds.",
            30, 12, 6, 10
    ),

    // Ground Buds
    GRITFANG(
            Elements.GROUND,
            "Gritfang",
            "A sturdy hatchling with a rocky shell, camouflaged among mountainous terrain.",
            38, 10, 12, 14
    ),
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
    GRAVL(
            Elements.GROUND,
            "Gravl",
            "A swift colt bounding through rocky canyons with ease.",
            38, 12, 9, 13
    ),
    QUAKE(
            Elements.GROUND,
            "Quake",
            "A cub with fur as dark as night, often seen prowling during dusk.",
            34, 9, 8, 12
    ),
    STONEWING(
            Elements.GROUND,
            "Stonewing",
            "A creature with a rocky shell, its wings adorned with precious stones.",
            36, 11, 10, 13
    ),

    // Electric Buds
    SPARK(
            Elements.ELECTRIC,
            "Spark",
            "A creature with a shell crackling with electricity, emitting sparks when threatened.",
            31, 10, 9, 11
    ),
    BLITZ(
            Elements.ELECTRIC,
            "Blitz",
            "A small creature crackling with electric energy, zapping unsuspecting targets.",
            31, 13, 7, 10
    ),
    JOLT(
            Elements.ELECTRIC,
            "Jolt",
            "A pulsating creature emitting electric shocks, capable of stunning foes with its charged attacks.",
            32, 14, 8, 11
    ),
    CRACKLE(
            Elements.ELECTRIC,
            "Crackle",
            "A crab crackling with electric energy, its claws sparking with each pinch.",
            36, 9, 10, 13
    ),
    ZAP(
            Elements.ELECTRIC,
            "Zap",
            "A tadpole crackling with electric energy, its movements generating sparks.",
            30, 10, 6, 10
    ),
    ION(
            Elements.ELECTRIC,
            "Ion",
            "A small creature crackling with electric energy, its fur standing on end.",
            30, 12, 7, 11
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
