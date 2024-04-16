package com.github.pokee.canvas;

import com.github.pokee.canvas.font.Font;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a drawing surface where pixels can be manipulated.
 */
public class PixelCanvas implements Canvas {

    protected final int defaultColor;

    protected final int width;
    protected final int height;

    /**
     * -1 = transparent
     * Otherwise RGB color for the pixel
     * -- GETTER --
     *
     * @return

     */
    @Getter
    protected final int[][] colors;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width        the width of the canvas
     * @param height       the height of the canvas
     * @param defaultColor the default color of the canvas
     */
    public PixelCanvas(final int width, final int height, final int defaultColor) {
        this.width = width;
        this.height = height;
        this.colors = new int[width][height];
        this.defaultColor = defaultColor;
    }

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public PixelCanvas(final int width, final int height) {
        this(width, height, 0x00000000);
    }

    /**
     * Draws a pixel at the specified coordinates with the given RGB color.
     *
     * @param x   the x-coordinate of the pixel
     * @param y   the y-coordinate of the pixel
     * @param rgb the RGB color of the pixel
     */
    @Override
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
    @Override
    public void drawImage(final int startX,
                          final int startY,
                          final BufferedImage image,
                          final boolean replaceTransparentPixels) {
        this.drawImage(startX, startY, image, replaceTransparentPixels, null);
    }

    /**
     * Draws an image onto the canvas at the specified coordinates.
     *
     * @param startX                   the starting x-coordinate
     * @param startY                   the starting y-coordinate
     * @param image                    the BufferedImage to draw
     * @param replaceTransparentPixels if true, replaces transparent pixels with the image pixels
     * @param replaceRGB               if not null, replaces the RGB color of the image with this color
     */
    @Override
    public void drawImage(final int startX,
                          final int startY,
                          final BufferedImage image,
                          final boolean replaceTransparentPixels,
                          final Color replaceRGB) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int rgb = image.getRGB(x, y);
                final int alpha = (rgb >> 24) & 0xFF;
                if (alpha == 0 && !replaceTransparentPixels) {
                    continue;
                }
                // check bounds
                final int imageX = startX + x;
                final int imageY = startY + y;
                if (imageX < 0 || imageX >= this.width || imageY < 0 || imageY >= this.height) {
                    continue;
                }
                final int drawRgb = replaceRGB != null ? replaceRGB.getRGB() : rgb;
                this.drawPixel(imageX, imageY, drawRgb);
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
    @Override
    public void fillRect(final int x,
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
    @Override
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
     * <p>
     * Don't ask me how this works, GitHub Copilot wrote this for me.
     * But it works, so I'm not complaining.
     * </p>
     *
     * @param startX    the starting x-coordinate of the line
     * @param startY    the starting y-coordinate of the line
     * @param endX      the ending x-coordinate of the line
     * @param endY      the ending y-coordinate of the line
     * @param thickness the thickness of the line
     * @param lineColor the RGB color of the line
     */
    @Override
    public void drawLine(final int startX,
                         final int startY,
                         final int endX,
                         final int endY,
                         final int thickness,
                         final int lineColor) {
        // Calculate the change in x and y
        final int deltaX = Math.abs(endX - startX);
        final int deltaY = Math.abs(endY - startY);

        // Determine the sign of the increment (positive or negative step)
        final int stepX = startX < endX ? 1 : -1;
        final int stepY = startY < endY ? 1 : -1;

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
     * Draws an ellipse on the canvas centered at (centerX, centerY) with the given x and y radii.
     *
     * @param centerX  the center x-coordinate of the ellipse
     * @param centerY  the center y-coordinate of the ellipse
     * @param radiusX  the horizontal radius of the ellipse
     * @param radiusY  the vertical radius of the ellipse
     * @param rgbColor the RGB color of the ellipse
     */
    public void strokeEllipse(int centerX, int centerY, int radiusX, int radiusY, int rgbColor) {
        int x, y;
        double d1, d2;
        x = 0;
        y = radiusY;

        // Initial decision parameter of region 1
        d1 = (radiusY * radiusY) - (radiusX * radiusX * radiusY) + (0.25 * radiusX * radiusX);
        @SuppressWarnings("ConstantValue") double dx = 2 * radiusY * radiusY * x;
        double dy = 2 * radiusX * radiusX * y;

        // For region 1
        while (dx < dy) {
            // Drawing points based on the 4-way symmetry
            drawPixel(centerX + x, centerY - y, rgbColor);
            drawPixel(centerX - x, centerY - y, rgbColor);
            drawPixel(centerX + x, centerY + y, rgbColor);
            drawPixel(centerX - x, centerY + y, rgbColor);

            // Checking and updating value of decision parameter based on algorithm
            if (d1 < 0) {
                x++;
                dx = dx + (2 * radiusY * radiusY);
                d1 = d1 + dx + (radiusY * radiusY);
            } else {
                x++;
                y--;
                dx = dx + (2 * radiusY * radiusY);
                dy = dy - (2 * radiusX * radiusX);
                d1 = d1 + dx - dy + (radiusY * radiusY);
            }
        }

        // Decision parameter of region 2
        d2 = ((radiusY * radiusY) * ((x + 0.5) * (x + 0.5))) +
                ((radiusX * radiusX) * ((y - 1) * (y - 1))) -
                (radiusX * radiusX * radiusY * radiusY);

        // Plotting points of region 2
        while (y >= 0) {
            // Drawing points based on the 4-way symmetry
            drawPixel(centerX + x, centerY - y, rgbColor);
            drawPixel(centerX - x, centerY - y, rgbColor);
            drawPixel(centerX + x, centerY + y, rgbColor);
            drawPixel(centerX - x, centerY + y, rgbColor);

            // Checking and updating parameter based on algorithm
            if (d2 > 0) {
                y--;
                dy = dy - (2 * radiusX * radiusX);
                d2 = d2 + (radiusX * radiusX) - dy;
            } else {
                y--;
                x++;
                dx = dx + (2 * radiusY * radiusY);
                dy = dy - (2 * radiusX * radiusX);
                d2 = d2 + dx - dy + (radiusX * radiusX);
            }
        }
    }

    /**
     * Fills an ellipse on the canvas centered at (centerX, centerY) with the given x and y radii.
     *
     * @param centerX  the center x-coordinate of the ellipse
     * @param centerY  the center y-coordinate of the ellipse
     * @param radiusX  the horizontal radius of the ellipse
     * @param radiusY  the vertical radius of the ellipse
     * @param rgbColor the RGB color of the ellipse
     */
    public void fillEllipse(int centerX, int centerY, int radiusX, int radiusY, int rgbColor) {
        int x, y;
        double d1, d2;
        x = 0;
        y = radiusY;

        // Initial decision parameter of region 1
        d1 = (radiusY * radiusY) - (radiusX * radiusX * radiusY) + (0.25 * radiusX * radiusX);
        @SuppressWarnings("ConstantValue") double dx = 2 * radiusY * radiusY * x;
        double dy = 2 * radiusX * radiusX * y;

        // For region 1
        while (dx < dy) {
            // Draw horizontal line from -x to x
            for (int i = centerX - x; i <= centerX + x; i++) {
                drawPixel(i, centerY + y, rgbColor);
                drawPixel(i, centerY - y, rgbColor);
            }

            // Checking and updating value of decision parameter based on algorithm
            if (d1 < 0) {
                x++;
                dx = dx + (2 * radiusY * radiusY);
                d1 = d1 + dx + (radiusY * radiusY);
            } else {
                x++;
                y--;
                dx = dx + (2 * radiusY * radiusY);
                dy = dy - (2 * radiusX * radiusX);
                d1 = d1 + dx - dy + (radiusY * radiusY);
            }
        }

        // Decision parameter of region 2
        d2 = ((radiusY * radiusY) * ((x + 0.5) * (x + 0.5))) +
                ((radiusX * radiusX) * ((y - 1) * (y - 1))) -
                (radiusX * radiusX * radiusY * radiusY);

        // Plotting points of region 2
        while (y >= 0) {
            // Draw horizontal line from -x to x
            for (int i = centerX - x; i <= centerX + x; i++) {
                drawPixel(i, centerY + y, rgbColor);
                drawPixel(i, centerY - y, rgbColor);
            }

            // Checking and updating parameter based on algorithm
            if (d2 > 0) {
                y--;
                dy = dy - (2 * radiusX * radiusX);
                d2 = d2 + (radiusX * radiusX) - dy;
            } else {
                y--;
                x++;
                dx = dx + (2 * radiusY * radiusY);
                dy = dy - (2 * radiusX * radiusX);
                d2 = d2 + dx - dy + (radiusX * radiusX);
            }
        }
    }

    /**
     * Resets the canvas to its default state.
     */
    @Override
    public void reset() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.colors[x][y] = this.defaultColor;
            }
        }
    }

    /**
     * Gets the height of the canvas.
     *
     * @return the height of the canvas
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the canvas.
     *
     * @return the width of the canvas
     */
    @Override
    public int getWidth() {
        return width;
    }

}
