package com.github.pokee.renderer.canvas.screen.result;

import com.github.pokee.renderer.canvas.screen.Screen;
import com.github.pokee.renderer.canvas.screen.ScreenManager;
import org.jetbrains.annotations.NotNull;

public class ReplaceRenderResult implements RenderResult {

    private final Screen newScreen;

    public ReplaceRenderResult(final @NotNull Screen newScreen) {
        this.newScreen = newScreen;
    }

    @Override
    public void execute(final ScreenManager manager) {
        manager.pop();
        manager.push(this.newScreen);
    }

}
