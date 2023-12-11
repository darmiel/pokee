package com.github.pokee.canvas.screen;

import com.github.pokee.canvas.canvas.Canvas;
import com.github.pokee.canvas.display.Renderable;
import com.github.pokee.canvas.screen.result.RenderResult;

import java.util.Stack;

public class ScreenManager {

    private final Canvas canvas;
    private final Stack<ScreenLike> screenStack;

    public ScreenManager(final Canvas canvas) {
        this.canvas = canvas;
        this.screenStack = new Stack<>();
    }

    public void push(final ScreenConsumer consumer) {
        this.screenStack.push(consumer);
    }

    public void push(final Screen screen) {
        this.screenStack.push(screen);
    }

    public void pop() {
        this.screenStack.pop();
    }

    public void render() {
        if (this.screenStack.empty()) {
            return;
        }
        final ScreenLike currentScreen = this.screenStack.peek();

        if (currentScreen instanceof final ScreenConsumer consumer) {
            this.screenStack.push(consumer.accept(this.canvas));
            this.render();
            return;
        }

        final Screen screen = (Screen) currentScreen;

        // a screen can return a result to be executed by the screen manager
        // this is useful for pushing, popping, or replacing screens
        final RenderResult result = screen.tickAll();
        if (result != null) {
            result.execute(this);
        }

        // if the screen is renderable, render it
        // TODO: handle if screen is null
        if (this.canvas instanceof final Renderable<?> renderable) {
            renderable.render();
        }
    }

}
