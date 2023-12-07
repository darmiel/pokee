package com.github.pokee.canvas.display;

public class ConsolePixelCanvas extends StringPixelCanvas {

    private final int newLines;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width     the width of the canvas
     * @param height    the height of the canvas
     * @param thickness multiplier for pixel representation
     * @param newLines  number of new lines to print before rendering
     */
    public ConsolePixelCanvas(final int width,
                              final int height,
                              final int thickness,
                              final int newLines) {
        super(width, height, thickness);

        this.newLines = newLines;
    }

    @Override
    public String render() {
        final String frame = super.render();
        System.out.println("\n".repeat(this.newLines) + frame);
        return frame;
    }

}
