package com.github.pokee.renderer.canvas.draw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AnsiColor {

    public static final String RESET = "\033[0m";

    public static String escape(final int fgRGB, final int bgRGB) {
        return "\033[38;2;" + splitRGB(fgRGB) + ";48;2;" + splitRGB(bgRGB) + "m";
    }

    private static String splitRGB(final int rgb) {
        final int r = (rgb >> 16) & 0xFF;
        final int g = (rgb >> 8) & 0xFF;
        final int b = rgb & 0xFF;
        return r + ";" + g + ";" + b;
    }

    public static Color darker(final Color input, final double factor) {
        return new Color(Math.max((int) (input.getRed() * factor), 0),
                Math.max((int) (input.getGreen() * factor), 0),
                Math.max((int) (input.getBlue() * factor), 0),
                input.getAlpha());
    }

    public static Color findMostCommon(final BufferedImage image, final Color defaultColor) {
        // find most common color for attacker
        final Map<Integer, Integer> colors = new HashMap<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int rgb = image.getRGB(x, y);
                if (((rgb >> 24) & 0xFF) == 0) {
                    continue;
                }
                if (((rgb >> 16) & 0xFF) > 0xAA && ((rgb >> 8) & 0xFF) > 0xAA && ((rgb) & 0xFF) > 0xAA) {
                    continue;
                }
                if (((rgb >> 16) & 0xFF) == 0 && ((rgb >> 8) & 0xFF) == 0 && ((rgb) & 0xFF) == 0) {
                    continue;
                }
                colors.put(rgb, colors.getOrDefault(rgb, 0) + 1);
            }
        }
        return new Color(colors.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Color.YELLOW.getRGB()));
    }
}
