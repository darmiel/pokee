package com.github.pokee.renderer.canvas.screen;

import com.github.pokee.renderer.canvas.animation.Animation;
import com.github.pokee.renderer.canvas.canvas.Canvas;
import com.github.pokee.renderer.canvas.screen.result.RenderResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen implements ScreenLike {

    protected long ticks = 0;
    protected final Canvas canvas;

    private final List<Animation> animationList;

    public Screen(final Canvas canvas) {
        this.canvas = canvas;

        this.animationList = new ArrayList<>();
    }

    protected <T extends Animation> T registerAnimation(final T animation) {
        this.animationList.add(animation);
        return animation;
    }

    protected void registerAnimation(final Animation ... animations) {
        this.animationList.addAll(List.of(animations));
    }

    private void tickAnimations() {
        for (final Animation animation : this.animationList) {
            if (animation.isDone()) {
                continue;
            }
            animation.tick();
        }
    }

    public @Nullable RenderResult tickAll() {
        this.ticks++;
        this.tickAnimations();
        if (this.resetCanvasOnDraw()) {
            this.canvas.reset();
        }
        return this.onDraw(this.canvas);
    }

    public abstract @Nullable RenderResult onDraw(final Canvas canvas);

    public boolean resetCanvasOnDraw() {
        return true;
    }

}
