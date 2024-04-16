package com.github.pokee.canvas.util;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils {

    private static String splitRGB(final int rgb) {
        final int r = (rgb >> 16) & 0xFF;
        final int g = (rgb >> 8) & 0xFF;
        final int b = rgb & 0xFF;
        return r + ";" + g + ";" + b;
    }

    public static java.awt.Color darker(final java.awt.Color input, final double factor) {
        return new java.awt.Color(Math.max((int) (input.getRed() * factor), 0),
                Math.max((int) (input.getGreen() * factor), 0),
                Math.max((int) (input.getBlue() * factor), 0),
                input.getAlpha());
    }

    public static java.awt.Color findMostCommon(final BufferedImage image, final java.awt.Color defaultColor) {
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
        return new java.awt.Color(colors.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(java.awt.Color.YELLOW.getRGB()));
    }

}
