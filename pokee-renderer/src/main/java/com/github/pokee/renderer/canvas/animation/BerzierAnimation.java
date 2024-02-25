package com.github.pokee.renderer.canvas.animation;

import java.util.function.Function;

public class BerzierAnimation implements Animation {

    /**
     * Number of ticks to complete the animation
     */
    private final int maxTick;

    /**
     * Function to calculate the value
     */
    private final Function<Float, Float> function;

    /**
     * Current tick
     */
    private int currentTick;

    /**
     * Current value
     */
    private float value;

    public BerzierAnimation(final int maxTick, final Function<Float, Float> function) {
        this.maxTick = maxTick;
        this.function = function;

        this.currentTick = 0;
    }

    public static BerzierAnimation easeIn(final int maxTick) {
        return new BerzierAnimation(maxTick, BerzierAnimation::easeIn);
    }

    public static BerzierAnimation easeOut(final int maxTick) {
        return new BerzierAnimation(maxTick, BerzierAnimation::easeOut);
    }

    public static BerzierAnimation blend(final int maxTick) {
        return new BerzierAnimation(maxTick, BerzierAnimation::blend);
    }

    @Override
    public void tick() {
        if (this.isDone()) {
            return;
        }
        this.currentTick++;

        final float t = (float) this.currentTick / (float) this.maxTick;
        this.value = this.function.apply(t);
    }

    @Override
    public boolean isDone() {
        return this.currentTick >= this.maxTick;
    }


    public float getValue() {
        return value;
    }

    public int getValue(final int start, final int end) {
        return (int) (start + (end - start) * this.value);
    }

    /**
     * Bezier curve
     *
     * @param t time from 0.0 to 1.0
     * @return value from 0 to 1
     */
    public static float blend(float t) {
        return t * t * (3.0f - 2.0f * t);
    }

    /**
     * Ease in
     *
     * @param t time from 0.0 to 1.0
     * @return value from 0.0 to 1.0
     */
    public static float easeIn(float t) {
        return t * t * t;
    }

    /**
     * Ease out
     *
     * @param t time from 0.0 to 1.0
     * @return value from 0.0 to 1.0
     */
    public static float easeOut(float t) {
        float invT = 1.0f - t;
        return 1.0f - invT * invT * invT;
    }


}
