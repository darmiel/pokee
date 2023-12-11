package com.github.pokee.canvas.screen.result;

import com.github.pokee.canvas.screen.Screen;
import com.github.pokee.canvas.screen.ScreenManager;
import org.jetbrains.annotations.NotNull;

public class PushRenderResult implements RenderResult {

    private final Screen newScreen;

    public PushRenderResult(final @NotNull Screen newScreen) {
        this.newScreen = newScreen;
    }

    @Override
    public void execute(final ScreenManager manager) {
        manager.push(this.newScreen);
    }

}
