package com.github.pokee.canvas;

import com.github.pokee.canvas.font.Font;

import java.awt.image.BufferedImage;

/**
 * Represents a drawing surface where pixels can be manipulated.
 */
public abstract class Canvas<T> {

    protected final int width;
    protected final int height;

    /**
     * -1 = transparent
     * Otherwise RGB color for the pixel
     */
    protected final int[][] colors;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public Canvas(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.colors = new int[width][height];
    }

    /**
     * Draws a pixel at the specified coordinates with the given RGB color.
     *
     * @param x   the x-coordinate of the pixel
     * @param y   the y-coordinate of the pixel
     * @param rgb the RGB color of the pixel
     */
    public void drawPixel(final int x, final int y, final int rgb) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
            this.colors[x][y] = rgb;
        }
    }

    /**
     * Draws an image onto the canvas at the specified coordinates.
     *
     * @param startX                   the starting x-coordinate
     * @param startY                   the starting y-coordinate
     * @param image                    the BufferedImage to draw
     * @param replaceTransparentPixels if true, replaces transparent pixels with the image pixels
     */
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

    /**
     * Draws a rectangle on the canvas.
     *
     * @param x      the x-coordinate for the upper-left corner of the rectangle
     * @param y      the y-coordinate for the upper-left corner of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param rgb    the RGB color of the rectangle
     */
    public void drawRect(final int x,
                         final int y,
                         final int width,
                         final int height,
                         final int rgb) {
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

    /**
     * Draws a filled square with a specified thickness around a central point to simulate a "thick" pixel.
     *
     * @param centerX        the x-coordinate of the central point
     * @param centerY        the y-coordinate of the central point
     * @param pointThickness the thickness of the square to draw
     * @param pointColor     the RGB color of the square
     */
    private void drawThickPoint(int centerX, int centerY, int pointThickness, int pointColor) {
        if (pointThickness <= 1) {
            this.drawPixel(centerX, centerY, pointColor);
            return;
        }
        // Calculate half the thickness to determine the bounds of the square
        int halfThickness = pointThickness / 2;
        for (int offsetY = -halfThickness; offsetY <= halfThickness; offsetY++) {
            for (int offsetX = -halfThickness; offsetX <= halfThickness; offsetX++) {
                this.drawPixel(centerX + offsetX, centerY + offsetY, pointColor);
            }
        }
    }

    /**
     * Draws text onto the canvas at the specified location.
     *
     * @param x    the x-coordinate for the text
     * @param y    the y-coordinate for the text
     * @param text the text to be drawn
     * @param font the font used for the text
     * @param rgb  the RGB color of the text
     * @return the width of the text in pixels
     */
    public int drawText(final int x,
                        final int y,
                        final String text,
                        final Font font,
                        final int rgb) {
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

    /**
     * Draws a line on the canvas between two points (start and end)
     * using the Bresenham's line drawing algorithm.
     *
     * @param startX    the starting x-coordinate of the line
     * @param startY    the starting y-coordinate of the line
     * @param endX      the ending x-coordinate of the line
     * @param endY      the ending y-coordinate of the line
     * @param thickness the thickness of the line
     * @param lineColor the RGB color of the line
     */
    public void drawLine(final int startX,
                         final int startY,
                         final int endX,
                         final int endY,
                         final int thickness,
                         final int lineColor) {
        // Calculate the change in x and y
        int deltaX = Math.abs(endX - startX);
        int deltaY = Math.abs(endY - startY);
        // Determine the sign of the increment (positive or negative step)
        int stepX = startX < endX ? 1 : -1;
        int stepY = startY < endY ? 1 : -1;

        // Initial error term for decision making
        int error = deltaX - deltaY;
        int errorAdjustment;

        int currentX = startX;
        int currentY = startY;

        while (true) {
            // Draw a filled square around the current point to simulate line thickness
            this.drawThickPoint(currentX, currentY, thickness, lineColor);

            // Check if the end of the line has been reached
            if (currentX == endX && currentY == endY) {
                break;
            }

            errorAdjustment = 2 * error;
            // Move horizontally or diagonally
            if (errorAdjustment > -deltaY) {
                error -= deltaY;
                currentX += stepX;
            }
            // Move vertically or diagonally
            if (errorAdjustment < deltaX) {
                error += deltaX;
                currentY += stepY;
            }
        }
    }

    /**
     * Resets the canvas to its default state.
     */
    public void reset() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.colors[x][y] = 0;
            }
        }
    }

    public abstract T render();

    /**
     * Gets the height of the canvas.
     *
     * @return the height of the canvas
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the canvas.
     *
     * @return the width of the canvas
     */
    public int getWidth() {
        return width;
    }
}
