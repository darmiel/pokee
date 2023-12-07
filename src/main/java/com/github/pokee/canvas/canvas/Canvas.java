package com.github.pokee.canvas.canvas;

import com.github.pokee.canvas.font.Font;

import java.awt.image.BufferedImage;

public interface Canvas {
    void drawPixel(int x, int y, int rgb);

    void drawImage(int startX,
                   int startY,
                   BufferedImage image,
                   boolean replaceTransparentPixels);

    void drawRect(int x,
                  int y,
                  int width,
                  int height,
                  int rgb);

    int drawText(int x,
                 int y,
                 String text,
                 Font font,
                 int rgb);

    void drawLine(int startX,
                  int startY,
                  int endX,
                  int endY,
                  int thickness,
                  int lineColor);

    void reset();

    int getHeight();

    int getWidth();
}
