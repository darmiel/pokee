package com.github.pokee.renderer.canvas.screen.screens;

import com.github.pokee.renderer.canvas.canvas.Canvas;
import com.github.pokee.renderer.canvas.font.Fonts;
import com.github.pokee.renderer.canvas.screen.Screen;
import com.github.pokee.renderer.canvas.screen.result.PopRenderResult;
import com.github.pokee.renderer.canvas.screen.result.RenderResult;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class RestartScreen extends Screen {

    public RestartScreen(Canvas canvas) {
        super(canvas);
    }

    @Override
    public @Nullable RenderResult onDraw(final Canvas canvas) {
        final long remainingTicks = 100 - this.ticks;
        canvas.fillRect(0, 0, canvas.getWidth(), canvas.getHeight(), Color.RED.getRGB());
        canvas.drawText(canvas.getWidth() / 2, canvas.getHeight() / 2, "LOL", Fonts.SMALL, Color.BLACK.getRGB());
        canvas.drawText(canvas.getWidth() / 2, canvas.getHeight() / 2 + 7, String.valueOf(remainingTicks), Fonts.SMALL, Color.BLACK.getRGB());
        if (remainingTicks <= 0) {
            return PopRenderResult.INSTANCE;
        }
        return null;
    }

}
