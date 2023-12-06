package com.github.pokee.canvas;

import com.github.pokee.canvas.font.Font;

import java.awt.image.BufferedImage;

public class Canvas {

    private final int width;
    private final int height;

    /**
     * -1 = transparent
     * Otherwise RGB color for the pixel
     */
    private final int[][] colors;

    public Canvas(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.colors = new int[width][height];
    }

    public void drawPixel(final int x, final int y, final int rgb) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
            this.colors[x][y] = rgb;
        }
    }

    public void drawImage(final int startX,
                          final int startY,
                          final BufferedImage image,
                          final boolean replaceTransparentPixels) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int rgb = image.getRGB(x, y);
                final int alpha = (rgb >> 24) & 0xFF;
                if (alpha == 0 && !replaceTransparentPixels) {
                    continue;
                }
                // check if white
                // TODO: remove this in the future, this is only a workaround since some sprites have white backgrounds
                if ((rgb & 0xFFFFFF) == 0xFFFFFF) {
                    continue;
                }
                // check bounds
                final int imageX = startX + x;
                final int imageY = startY + y;
                if (imageX < 0 || imageX >= this.width || imageY < 0 || imageY >= this.height) {
                    continue;
                }
                this.drawPixel(imageX, imageY, rgb);
            }
        }
    }

    public void drawRect(final int x, final int y, final int width, final int height, final int rgb) {
        for (int dY = y; dY < y + height; dY++) {
            if (dY >= this.height) {
                continue;
            }
            for (int dX = x; dX < x + width; dX++) {
                if (dX >= this.width) {
                    continue;
                }
                this.drawPixel(dX, dY, rgb);
            }
        }
    }

    public int drawText(final int x, final int y, final String text, final Font font, final int rgb) {
        // get character map
        final boolean[][] characterMap = font.getCharacterMap(text);
        for (int cX = 0; cX < characterMap.length; cX++) {
            for (int cY = 0; cY < characterMap[cX].length; cY++) {
                if (characterMap[cX][cY]) {
                    this.drawPixel(x + cX, y + cY, rgb);
                }
            }
        }
        return characterMap.length;
    }

    public void drawLine(final int startX, final int startY, final int endX, final int endY) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void reset() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.colors[x][y] = 0;
            }
        }
    }

    public String render(final int thickness) {
        final StringBuilder bob = new StringBuilder();

        final String pixel = " ".repeat(thickness);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                final int color = this.colors[x][y];

                // don't render transparent pixels
                final int alpha = color >> 24 & 0xFF;
                if (alpha == 0) {
                    bob.append(pixel);
                    continue;
                }

                bob.append(AnsiColor.escape(0xFFFFFF, color)).append(pixel).append(AnsiColor.RESET);
            }
            bob.append("\n");
        }
        return bob.toString();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
