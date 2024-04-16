package com.github.pokee.canvas.animation;

import java.awt.*;

public class DamageAnimation implements Animation {

    private final int ticks;

    private int currentTick;

    public DamageAnimation(int ticks) {
        this.ticks = ticks;
        this.reset();
    }

    int state = 0;

    @Override
    public void tick() {
        if (--this.currentTick <= 0) {
            this.currentTick = this.ticks;
            if (++this.state >= 3) {
                this.state = 0;
            }
        }
    }

    public void reset() {
        this.currentTick = this.ticks;
        this.state = 0;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    public Color getColor() {
        return switch (this.state) {
            case 1 -> Color.RED;
            case 2 -> Color.ORANGE;
            default -> null;
        };
    }

}
