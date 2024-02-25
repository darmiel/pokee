package com.github.pokee.renderer.canvas.screen;

import com.github.pokee.renderer.canvas.canvas.Canvas;

public interface ScreenConsumer extends ScreenLike {

    Screen accept(final Canvas canvas);

}
