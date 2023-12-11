package com.github.pokee.canvas.display;

import com.github.pokee.canvas.canvas.PixelCanvas;
import com.github.pokee.canvas.font.Font;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugPixelCanvas extends PixelCanvas {

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public DebugPixelCanvas(final int width,
                            final int height) {
        super(width, height, 1);
    }


    @Override
    public void drawPixel(int x, int y, int rgb) {
        System.out.println("drawPixel(" + x + ", " + y + ", " + rgb + ")");
        super.drawPixel(x, y, rgb);
    }

    @Override
    public void drawImage(int startX, int startY, BufferedImage image, boolean replaceTransparentPixels) {
        System.out.println("drawImage(" + startX + ", " + startY + ", " + image + ", " + replaceTransparentPixels + ")");
        super.drawImage(startX, startY, image, replaceTransparentPixels);
    }

    @Override
    public void drawImage(int startX, int startY, BufferedImage image, boolean replaceTransparentPixels, Color replaceRGB) {
        System.out.println("drawImage(" + startX + ", " + startY + ", " + image + ", " + replaceTransparentPixels + ", " + replaceRGB + ")");
        super.drawImage(startX, startY, image, replaceTransparentPixels, replaceRGB);
    }

    @Override
    public void fillRect(int x, int y, int width, int height, int rgb) {
        System.out.println("fillRect(" + x + ", " + y + ", " + width + ", " + height + ", " + rgb + ")");
        super.fillRect(x, y, width, height, rgb);
    }

    @Override
    public int drawText(int x, int y, String text, Font font, int rgb) {
        System.out.println("drawText(" + x + ", " + y + ", " + text + ", " + font + ", " + rgb + ")");
        return super.drawText(x, y, text, font, rgb);
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY, int thickness, int lineColor) {
        System.out.println("drawLine(" + startX + ", " + startY + ", " + endX + ", " + endY + ", " + thickness + ", " + lineColor + ")");
        super.drawLine(startX, startY, endX, endY, thickness, lineColor);
    }

    @Override
    public void strokeEllipse(int centerX, int centerY, int radiusX, int radiusY, int rgbColor) {
        System.out.println("strokeEllipse(" + centerX + ", " + centerY + ", " + radiusX + ", " + radiusY + ", " + rgbColor + ")");
        super.strokeEllipse(centerX, centerY, radiusX, radiusY, rgbColor);
    }

    @Override
    public void fillEllipse(int centerX, int centerY, int radiusX, int radiusY, int rgbColor) {
        System.out.println("fillEllipse(" + centerX + ", " + centerY + ", " + radiusX + ", " + radiusY + ", " + rgbColor + ")");
        super.fillEllipse(centerX, centerY, radiusX, radiusY, rgbColor);
    }

}
