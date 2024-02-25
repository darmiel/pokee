package com.github.pokee.renderer.canvas.screen.result;

import com.github.pokee.renderer.canvas.screen.ScreenManager;

public class PopRenderResult implements RenderResult {

    public static final PopRenderResult INSTANCE = new PopRenderResult();

    public PopRenderResult() {

    }

    @Override
    public void execute(final ScreenManager manager) {
        manager.pop();
    }

}
