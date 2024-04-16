package com.github.pokee.canvas.util.console;

import com.github.pokee.canvas.Renderable;
import com.github.pokee.canvas.PixelCanvas;

public class StringPixelCanvas extends PixelCanvas implements Renderable<String> {

    private final int thickness;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public StringPixelCanvas(final int width, final int height, final int thickness) {
        super(width, height);

        this.thickness = thickness;
    }

    /**
     * Renders the canvas content, using thickness to display pixels.
     *
     * @param thickness multiplier for pixel representation
     */
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

    /**
     * Renders the canvas content, using a thickness of 1.
     *
     * @return the rendered canvas
     */
    @Override
    public String render() {
        return this.render(this.thickness);
    }

}
