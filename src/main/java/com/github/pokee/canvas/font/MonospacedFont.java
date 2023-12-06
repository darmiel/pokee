package com.github.pokee.canvas.font;

import java.awt.image.BufferedImage;

/**
 * Represents a monospaced font with glyphs defined by a boolean grid.
 */
public class MonospacedFont implements Font {

    public static final String GLYPH_MAPPING = "abcdefghijklmnopqrstuvwxyz0123456789";

    private final int glyphWidth;
    private final int glyphHeight;
    private final int padding;
    private final boolean[][][] glyphs;

    /**
     * Constructs a monospaced font with the specified glyph dimensions and padding.
     *
     * @param glyphWidth  the width of each glyph
     * @param glyphHeight the height of each glyph
     * @param padding     the padding between glyphs
     * @param glyphs      a three-dimensional array of booleans representing the glyphs
     */
    public MonospacedFont(final int glyphWidth,
                          final int glyphHeight,
                          final int padding,
                          final boolean[][][] glyphs) {
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        this.padding = padding;
        this.glyphs = glyphs;
    }

    /**
     * Extracts a single character as a boolean grid from an image starting at the specified coordinates.
     *
     * @param startX the x-coordinate of the top-left corner from where to start extracting
     * @param width  the width of the character to extract
     * @param height the height of the character to extract
     * @param image  the image containing the character
     * @return a two-dimensional array of booleans representing the character
     */
    private static boolean[][] extractCharacterFromImage(final int startX,
                                                         final int width,
                                                         final int height,
                                                         final BufferedImage image) {
        final boolean[][] result = new boolean[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int rgb = image.getRGB(x + startX, y);
                result[x][y] = (rgb >> 24) != 0;
            }
        }
        return result;
    }

    /**
     * Creates a MonospacedFont from an image containing glyphs, using a specified glyph width and padding.
     *
     * @param glyphWidth the width of each glyph in the image
     * @param padding    the padding between glyphs in the image
     * @param image      the BufferedImage containing the glyphs
     * @return a new MonospacedFont instance
     */
    public static MonospacedFont fromImage(final int glyphWidth, final int padding, final BufferedImage image) {
        // glyph height is the height of the image
        final int glyphHeight = image.getHeight();

        // read every character from the image
        final boolean[][][] characters = new boolean[MonospacedFont.GLYPH_MAPPING.length()][glyphWidth][glyphHeight];
        int index = 0;
        for (int x = 0; x < image.getWidth(); x += glyphWidth + 1) {
            final boolean[][] character = MonospacedFont.extractCharacterFromImage(x, glyphWidth, glyphHeight, image);
            characters[index++] = character;
        }

        return new MonospacedFont(glyphWidth, glyphHeight, padding, characters);
    }

    /**
     * Returns a boolean grid representing mapped characters for the given text
     * using the monospaced font.
     *
     * @param text the text to be translated into a character map
     * @return a two-dimensional array of booleans representing the characters of the text
     */
    @Override
    public boolean[][] getCharacterMap(final String text) {
        final boolean[][] result = new boolean[(this.glyphWidth + this.padding) * text.length()][this.glyphHeight];

        final char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            final char character = characters[i];

            final int index = MonospacedFont.GLYPH_MAPPING.indexOf(Character.toLowerCase(character));
            if (index < 0) {
                System.out.println("cannot find " + character + " in " + MonospacedFont.GLYPH_MAPPING);
                continue;
            }
            final boolean[][] glyph = this.glyphs[index];
            for (int x = 0; x < glyph.length; x++) {
                System.arraycopy(glyph[x], 0, result[x + i * (this.glyphWidth + this.padding)], 0, glyph[x].length);
            }
        }

        return result;
    }

    /**
     * Calculates the width of the given text using the glyphs and padding of the monospaced font.
     *
     * @param text the text for which to calculate the width
     * @return the pixel width of the text
     */
    @Override
    public int getWidth(String text) {
        return (this.glyphWidth + this.padding) * text.length();
    }

}
