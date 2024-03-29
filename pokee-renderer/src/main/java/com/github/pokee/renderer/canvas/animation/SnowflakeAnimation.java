package com.github.pokee.renderer.canvas.animation;

import com.github.pokee.renderer.canvas.canvas.Canvas;

public class SnowflakeAnimation implements Animation {

    private final int width;
    private final int height;
    private final boolean[][] snowflakes;
    private final int[] rowColors;
    private final float amount;

    public SnowflakeAnimation(final int width, final int height, final int[] colors, final float amount) {
        this.width = width;
        this.height = height;
        this.amount = amount;

        this.snowflakes = new boolean[width][height];
        this.rowColors = new int[width];
        for (int i = 0; i < width; i++) {
            this.rowColors[i] = colors[(int) (Math.random() * colors.length)];
        }

        this.fillRandom();
    }

    private void fillRandom() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.snowflakes[x][y]) {
                    continue;
                }
                if (Math.random() < this.amount) {
                    this.snowflakes[x][y] = true;
                }
            }
        }
    }

    private void moveSnowflakes() {
        for (int x = 0; x < this.width; x++) {
            for (int y = this.height - 1; y >= 0; y--) {
                if (!this.snowflakes[x][y]) {
                    continue;
                }
                this.snowflakes[x][y] = false;
                if (y + 1 < this.height) {
                    this.snowflakes[x][y + 1] = true;
                } else {
                    this.snowflakes[x][0] = true;
                }
            }
        }
    }

    public void render(final Canvas canvas) {
        for (int x = 0; x < this.width; x++) {
            for (int y = this.height - 1; y >= 0; y--) {
                if (!this.snowflakes[x][y]) {
                    continue;
                }
                final int color = this.rowColors[x];
                canvas.drawPixel(x, y, color);
            }
        }
    }

    public void renderReversed(final Canvas canvas) {
        for (int x = 0; x < this.width; x++) {
            for (int y = this.height - 1; y >= 0; y--) {
                if (!this.snowflakes[x][y]) {
                    continue;
                }
                final int color = this.rowColors[x];
                canvas.drawPixel(x, this.height - y, color);
            }
        }
    }

    @Override
    public void tick() {
//        this.fillRandom();
        this.moveSnowflakes();
    }

    @Override
    public boolean isDone() {
        return false;
    }

}
