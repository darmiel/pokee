package com.github.pokee.canvas.util;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Getter
public class DrawablePokemon {

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

    public DrawablePokemon(final int pokedexNumber) throws IOException {
        // read images from sprites
        final SpriteType[] spriteTypes = SpriteType.values();
        this.sprites = new BufferedImage[spriteTypes.length];
        for (int i = 0; i < spriteTypes.length; i++) {
            final BufferedImage image = DrawablePokemon.getPokemonSprite(pokedexNumber, spriteTypes[i]);
            this.sprites[i] = image;
        }
    }

    public BufferedImage getSprite(final SpriteType spriteType) {
        return this.sprites[spriteType.ordinal()];
    }

}
