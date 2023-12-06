package com.github.pokee.canvas.draw;

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

}
