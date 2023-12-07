package com.github.pokee.canvas.animation;

public class BounceAnimation implements Animation {

    private final int startY;
    private final int endY;

    private boolean direction;
    private int y;

    public BounceAnimation(final int startY, final int endY) {
        this.startY = startY;
        this.endY = endY;

        this.y = this.startY;
        this.direction = this.endY > this.startY;
    }

    @Override
    public void tick() {
        if (this.direction) {
            if (++this.y >= this.endY) {
                this.direction = false;
            }
        } else {
            if (--this.y <= this.startY) {
                this.direction = true;
            }
        }
    }

    @Override
    public boolean isDone() {
        return false; // this is an infinite animation
    }

    public int getY() {
        return y;
    }

    public int getEndY() {
        return endY;
    }

}
