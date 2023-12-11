package com.github.pokee.canvas.screen;

import com.github.pokee.canvas.canvas.Canvas;

public interface ScreenConsumer extends ScreenLike {

    Screen accept(final Canvas canvas);

}
