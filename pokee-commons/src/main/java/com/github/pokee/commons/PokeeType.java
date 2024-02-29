package com.github.pokee.commons;

public enum PokeeType {

    FLAREPUP("FlarePup", ElementType.FIRE, 30, 10, 5, 10, "A small canine creature with a fiery tail that illuminates paths in the dark."),
    AQUAFLUFF("AquaFluff", ElementType.WATER, 35, 8, 7, 12, "A fluffy aquatic creature with water-repellent fur that loves to play in the waves."),
    LEAFCUB("LeafCub", ElementType.GRASS, 32, 7, 9, 11, "A small cub covered in green leaves, blending seamlessly with its forest habitat."),
    ROCKHATCH("RockHatch", ElementType.GROUND, 38, 10, 12, 14, "A sturdy hatchling with a rocky shell, camouflaged among mountainous terrain."),
    THUNDERCHICK("ThunderChick", ElementType.ELECTRIC, 30, 12, 6, 10, "A tiny chick emitting sparks of electricity, often found perched atop lightning rods."),
    FROSTLING("Frostling", ElementType.WATER, 28, 6, 8, 9, "A small icy creature with a chilly demeanor, frolicking in snowy landscapes."),
    MAGMITE("Magmite", ElementType.FIRE, 33, 11, 7, 11, "A molten creature emitting intense heat, found near volcanic vents."),
    SOAROWL("SoarOwl", ElementType.GRASS, 29, 9, 5, 10, "A graceful owl with wings as wide as the sky, soaring effortlessly through clouds."),
    TIDALFIN("TidalFin", ElementType.WATER, 34, 8, 6, 11, "A sleek aquatic creature with fins adapted for swift underwater movements."),
    BRAMBLEHOG("BrambleHog", ElementType.GRASS, 36, 9, 10, 13, "A spiky hedgehog covered in thorny brambles, defending its territory fiercely."),
    STONESPARK("StoneSpark", ElementType.GROUND, 37, 11, 9, 13, "A sparkly creature composed of glittering stones, blending in with rocky landscapes."),
    SHOCKLING("Shockling", ElementType.ELECTRIC, 31, 13, 7, 10, "A small creature crackling with electric energy, zapping unsuspecting targets."),
    GLACICLE("Glacicle", ElementType.WATER, 27, 7, 9, 9, "A shard of ice with a frigid aura, found in icy caverns and frozen lakes."),
    EMBERCHICK("EmberChick", ElementType.FIRE, 32, 12, 6, 11, "A small chick surrounded by flickering flames, warming its surroundings."),
    WINDWHISK("WindWhisk", ElementType.GRASS, 28, 10, 5, 10, "A whiskered creature with aerodynamic features, riding air currents effortlessly."),
    SURFSWIFT("SurfSwift", ElementType.WATER, 33, 9, 7, 11, "A swift aquatic creature capable of riding the crest of ocean waves with ease."),
    FOLIAGEFERRET("FoliageFerret", ElementType.GRASS, 35, 8, 11, 13, "A nimble ferret adorned with lush foliage, darting through dense underbrush."),
    MOUNTAINMARBLE("MountainMarble", ElementType.GROUND, 39, 12, 10, 14, "A sturdy creature with a marble-like hide, blending into rocky terrain effortlessly."),
    JOLTPULSE("JoltPulse", ElementType.ELECTRIC, 32, 14, 8, 11, "A pulsating creature emitting electric shocks, capable of stunning foes with its charged attacks."),
    SNOWSPRITE("SnowSprite", ElementType.WATER, 29, 8, 10, 10, "A mischievous sprite made of snowflakes, dancing through winter storms."),
    FLAMELEAF("FlameLeaf", ElementType.FIRE, 34, 13, 7, 12, "A leafy creature surrounded by flickering flames, radiating warmth wherever it goes."),
    BREEZEOWL("BreezeOwl", ElementType.GRASS, 30, 11, 6, 10, "A wise owl with feathers as light as a breeze, gliding through the skies with grace."),
    CASCADELARK("CascadeLark", ElementType.WATER, 35, 10, 8, 12, "A melodious bird with iridescent feathers, singing by cascading waterfalls."),
    VINEVIPER("VineViper", ElementType.GRASS, 37, 9, 12, 14, "A serpentine creature entwined with vines, lurking in dense jungles."),
    CANYONCOLT("CanyonColt", ElementType.GROUND, 38, 12, 9, 13, "A swift colt bounding through rocky canyons with ease."),
    ZEPHYRWING("ZephyrWing", ElementType.GRASS, 31, 11, 7, 11, "A winged creature with feathers as light as the wind, soaring gracefully through the air."),
    DUSKCUB("DuskCub", ElementType.GROUND, 34, 9, 8, 12, "A cub with fur as dark as night, often seen prowling during dusk."),
    CRIMSONCHIRP("CrimsonChirp", ElementType.FIRE, 32, 10, 6, 11, "A tiny bird with crimson feathers, emitting heat from its tiny body."),
    SURFSTREAM("SurfStream", ElementType.WATER, 33, 10, 7, 11, "A sleek aquatic creature that surfs swiftly along ocean currents."),
    VINEVIGOR("VineVigor", ElementType.GRASS, 36, 10, 11, 13, "A creature empowered by the vitality of lush vines, thriving in verdant forests."),
    MAGMAMOTH("MagmaMoth", ElementType.FIRE, 35, 14, 8, 12, "A fiery moth with wings ablaze, fluttering around volcanic regions."),
    CIRRUSCLAW("CirrusClaw", ElementType.GRASS, 30, 12, 6, 10, "A creature with razor-sharp claws and feathered wings, soaring among wispy clouds."),
    TIDALSPARK("TidalSpark", ElementType.WATER, 34, 11, 7, 12, "A creature crackling with energy, harnessing the power of ocean tides."),
    FROSTFANG("FrostFang", ElementType.WATER, 31, 9, 11, 11, "A creature with icy fangs, capable of freezing its prey with a single bite."),
    VINEVOLT("VineVolt", ElementType.GRASS, 30, 11, 8, 12, "A creature charged with electric energy, vines crackling with power as it moves."),
    MAGMAPEBBLE("MagmaPebble", ElementType.FIRE, 34, 10, 8, 12, "A small pebble infused with fiery energy, glowing red-hot."),
    CRACKLECRAB("CrackleCrab", ElementType.ELECTRIC, 36, 9, 10, 13, "A crab crackling with electric energy, its claws sparking with each pinch."),
    MARSHMARVEL("MarshMarvel", ElementType.WATER, 32, 11, 9, 12, "A mysterious creature dwelling in marshlands, its body composed of shimmering water."),
    VOLTIVINE("VoltiVine", ElementType.GRASS, 31, 12, 7, 11, "A vine-covered creature pulsating with electric energy, its presence electrifying the air."),
    FLAMEFERRET("FlameFerret", ElementType.FIRE, 33, 11, 6, 11, "A ferret with fur ablaze, leaving a trail of fire wherever it goes."),
    GROUNDPUP("GroundPup", ElementType.GROUND, 35, 9, 12, 14, "A playful puppy with earthy fur, digging tunnels with boundless energy."),
    SHOCKSHELL("ShockShell", ElementType.ELECTRIC, 31, 10, 9, 11, "A creature with a shell crackling with electricity, emitting sparks when threatened."),
    SPLASHSPROUT("SplashSprout", ElementType.WATER, 34, 8, 10, 12, "A tiny seedling with leaves resembling water lilies, thriving in wetlands."),
    BLAZEBEETLE("BlazeBeetle", ElementType.FIRE, 37, 12, 8, 13, "A beetle with a fiery carapace, emitting intense heat as it scuttles across the ground."),
    FOLIAGEFROST("FoliageFrost", ElementType.GRASS, 29, 7, 11, 10, "A creature with frost-covered foliage, leaving icy trails as it moves."),
    THUNDERTADPOLE("ThunderTadpole", ElementType.ELECTRIC, 30, 10, 6, 10, "A tadpole crackling with electric energy, its movements generating sparks."),
    GEYSERGUP("GeyserGup", ElementType.WATER, 36, 8, 11, 13, "A creature resembling a geyser, shooting jets of water high into the air."),
    FLAREFLUFF("FlareFluff", ElementType.FIRE, 32, 11, 6, 11, "A fluffy creature emitting warm, comforting heat from its fur."),
    TERRAPUP("TerraPup", ElementType.GROUND, 34, 10, 9, 12, "A puppy with rocky fur, its earthy scent calming those around it."),
    THUNDERLING("Thunderling", ElementType.ELECTRIC, 30, 12, 7, 11, "A small creature crackling with electric energy, its fur standing on end."),
    TIDALTOAD("TidalToad", ElementType.WATER, 35, 9, 10, 12, "A toad capable of summoning tidal waves with a croak, its skin glistening with water.");
    private final String name;
    private final ElementType type;
    private final String description;
    private final int baseHP;
    private final int baseAttack;
    private final int baseDefense;
    private final int maxLevel;
    PokeeType(String name, ElementType type, int baseHP, int baseAttack, int baseDefense, int maxLevel, String description) {
        this.name = name;
        this.type = type;
        this.baseHP = baseHP;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.maxLevel = maxLevel;
        this.description = description;
    };
}
