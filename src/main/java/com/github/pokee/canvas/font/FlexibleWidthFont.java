package com.github.pokee.canvas.font;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a font with variable-width glyphs that can be used for drawing text on a canvas.
 */
public class FlexibleWidthFont implements Font {

    /**
     * A record representing a glyph with a specific width and pixel data.
     */
    record Glyph(int width, boolean[][] data) {

    }

    public static final String GLYPH_MAPPING = "abcdefghijklmnopqrstuvwxyz0123456789.:+- ";
    private final Glyph[] glyphs;

    private final int glyphHeight;
    private final int padding;

    /**
     * Constructs a FlexibleWidthFont with the specified glyph height, padding between glyphs,
     * and an array of Glyph objects.
     *
     * @param glyphHeight the height of each glyph in the font
     * @param padding     the padding between glyphs when rendering text
     * @param glyphs      an array of Glyph objects representing each character in the font
     */
    public FlexibleWidthFont(final int glyphHeight,
                             final int padding,
                             final Glyph[] glyphs) {
        this.glyphHeight = glyphHeight;
        this.padding = padding;
        this.glyphs = glyphs;
    }

    /**
     * Creates a FlexibleWidthFont from a BufferedImage where glyphs are separated by red lines.
     *
     * @param padding the padding between glyphs when rendering text
     * @param image   a BufferedImage containing the glyph data
     * @return a new instance of FlexibleWidthFont created from the image
     */
    public static FlexibleWidthFont fromImage(final int padding, final BufferedImage image) {
        // glyph height is the height of the image
        final int glyphHeight = image.getHeight();

        int glyphIndex = 0;
        final Glyph[] glyphs = new Glyph[FlexibleWidthFont.GLYPH_MAPPING.length()];

        final List<Boolean[]> currentGlyphData = new ArrayList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            // get first pixel to check for end of glyph
            final int checkPixel = image.getRGB(x, 0);

            // if the checked pixel is red, we reached the end of the glyph
            // -> extract pixels, create glyph and add it to the list
            final boolean isEndOfGlyph = ((checkPixel >> 16) & 0xFF) >= 127;
            if (isEndOfGlyph) {
                if (currentGlyphData.size() == 0) {
                    continue;
                }
                final boolean[][] glyphData = new boolean[currentGlyphData.size()][];
                for (int i = 0; i < currentGlyphData.size(); i++) {
                    final Boolean[] currentGlyphColumnData = currentGlyphData.get(i);
                    for (int j = 0; j < currentGlyphColumnData.length; j++) {
                        if (glyphData[i] == null) {
                            glyphData[i] = new boolean[currentGlyphColumnData.length];
                        }
                        glyphData[i][j] = currentGlyphColumnData[j];
                    }
                }
                final Glyph glyph = new Glyph(currentGlyphData.size(), glyphData);
                glyphs[glyphIndex++] = glyph;
                currentGlyphData.clear();
                continue;
            }

            // otherwise check every pixel in the column and add it to the current glyph data
            // non-transparent pixels are "added", the color does not matter
            final Boolean[] column = new Boolean[image.getHeight()];
            for (int y = 0; y < image.getHeight(); y++) {
                final int rgb = image.getRGB(x, y);
                final boolean isTransparent = (rgb >> 24) == 0;
                column[y] = !isTransparent;
            }

            currentGlyphData.add(column);
        }

        return new FlexibleWidthFont(glyphHeight, padding, glyphs);
    }

    /**
     * Creates a FlexibleWidthFont from an image file. This method is unsafe because it throws
     * a RuntimeException on failure to read the file.
     *
     * @param padding  the padding between glyphs when rendering text
     * @param fileName the name of the file containing the glyph data
     * @return a new instance of FlexibleWidthFont created from the image file
     * @throws RuntimeException if reading the file fails
     */
    public static FlexibleWidthFont fromImageUnsafe(final int padding, final String fileName) {
        final File file = new File(fileName);
        final BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return FlexibleWidthFont.fromImage(padding, image);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean[][] getCharacterMap(final String text) {
        final char[] characters = text.toLowerCase().toCharArray();
        if (characters.length == 0) {
            return new boolean[0][0];
        }

        // first, we need to calculate the width of the resulting image
        int width = 0;
        for (final char character : characters) {
            final int glyphIndex = FlexibleWidthFont.GLYPH_MAPPING.indexOf(character);
            if (glyphIndex == -1) {
                throw new IllegalArgumentException("Character " + character + " is not supported");
            }
            final Glyph glyph = this.glyphs[glyphIndex];
            width += (width > 0 ? this.padding : 0) + glyph.width();
        }

        // now we can create the resulting image
        final boolean[][] result = new boolean[width][this.glyphHeight];
        int currentX = 0;
        for (final char character : characters) {
            final Glyph glyph = this.glyphs[FlexibleWidthFont.GLYPH_MAPPING.indexOf(character)];
            for (int x = 0; x < glyph.width(); x++) {
                System.arraycopy(glyph.data()[x], 0, result[currentX + x], 0, glyphHeight);
            }
            currentX += glyph.width() + this.padding;
        }

        return result;
    }

}
