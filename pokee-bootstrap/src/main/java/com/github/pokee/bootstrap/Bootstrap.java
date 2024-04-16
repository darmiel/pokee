package com.github.pokee.bootstrap;

import com.github.pokee.canvas.Canvas;
import com.github.pokee.canvas.DebugPixelCanvas;
import com.github.pokee.canvas.screen.ScreenManager;
import com.github.pokee.canvas.screen.screens.AttackScreen;
import com.github.pokee.canvas.screen.screens.RestartScreen;
import com.github.pokee.canvas.util.console.ConsolePixelCanvas;
import com.github.pokee.canvas.util.jframe.JFramePixelCanvas;

import java.io.IOException;

public class Bootstrap {

    public static final int FPS = 20;


    public static void main(String[] args) throws IOException, InterruptedException {
        final Canvas canvas;
        if (args.length > 0 && args[0].equals("frame")) {
            final int width = 250;
            canvas = new JFramePixelCanvas(width, (int)((float) width / 16.0) * 9, 3);
        } else if (args.length > 0 && args[0].equals("debug")) {
            canvas = new DebugPixelCanvas(150, 70);
        } else {
            canvas = new ConsolePixelCanvas(150, 70, 2, 8);
        }

        final ScreenManager screenManager = new ScreenManager(canvas);
        screenManager.push(RestartScreen::new);
        screenManager.push(new AttackScreen(canvas, 25, 6));
        //screenManager.push(GameLoopScreen::new);

        //noinspection InfiniteLoopStatement
        for (; ; ) {
            screenManager.render();
            //noinspection BusyWait
            Thread.sleep(1000 / FPS);
        }
    }

}
