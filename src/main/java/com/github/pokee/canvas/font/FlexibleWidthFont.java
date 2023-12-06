package com.github.pokee.canvas.font;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FlexibleWidthFont implements Font {


    record Glyph(int width, boolean[][] data) {

    }

    public static final String GLYPH_MAPPING = "abcdefghijklmnopqrstuvwxyz0123456789.:+- ";
    private final Glyph[] glyphs;

    private final int glyphHeight;
    private final int padding;

    public FlexibleWidthFont(final int glyphHeight,
                             final int padding,
                             final Glyph[] glyphs) {
        this.glyphHeight = glyphHeight;
        this.padding = padding;
        this.glyphs = glyphs;
    }

    public static FlexibleWidthFont fromImage(final int glyphWidth, final int padding, final BufferedImage image) {
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
