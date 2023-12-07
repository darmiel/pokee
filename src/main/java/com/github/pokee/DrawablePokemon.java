package com.github.pokee;

import com.github.pokee.canvas.draw.SpriteType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DrawablePokemon extends Pokemon {

    private static final File missingNoFile = new File("sprites", "MissingNo.png");

    private final BufferedImage[] sprites;

    private static BufferedImage getPokemonSprite(final int pokedexNumber,
                                                  final SpriteType spriteType) throws IOException {
        final File file = Paths.get("sprites", spriteType.getDirName(), pokedexNumber + ".png").toFile();
        if (!file.exists()) {
            return ImageIO.read(DrawablePokemon.missingNoFile);
        }
        return ImageIO.read(file);
    }

    public DrawablePokemon(final int pokedexNumber,
                           final String name,
                           final PokemonType type) throws IOException {
        super(pokedexNumber, name, type);

        // read images from sprites
        final SpriteType[] spriteTypes = SpriteType.values();
        this.sprites = new BufferedImage[spriteTypes.length];
        for (int i = 0; i < spriteTypes.length; i++) {
            final BufferedImage image = DrawablePokemon.getPokemonSprite(pokedexNumber, spriteTypes[i]);
            this.sprites[i] = image;
        }
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }
}
