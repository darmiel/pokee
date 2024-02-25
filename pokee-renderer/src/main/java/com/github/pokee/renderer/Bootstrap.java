package com.github.pokee.renderer;

import com.github.pokee.renderer.canvas.canvas.Canvas;
import com.github.pokee.renderer.canvas.display.ConsolePixelCanvas;
import com.github.pokee.renderer.canvas.display.DebugPixelCanvas;
import com.github.pokee.renderer.canvas.display.JFramePixelCanvas;
import com.github.pokee.renderer.canvas.screen.ScreenManager;
import com.github.pokee.renderer.canvas.screen.screens.AttackScreen;
import com.github.pokee.renderer.canvas.screen.screens.RestartScreen;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class Bootstrap {

    public static final int FPS = 20;

    public static final Random random = new SecureRandom();

    private static void run(final Canvas canvas) throws IOException, InterruptedException {
        final int attacker = random.nextInt(20 + 1) - 1;
        final int defender = random.nextInt(20 + 1) - 1;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final Canvas canvas;
        if (args.length > 0 && args[0].equals("frame")) {
            canvas = new JFramePixelCanvas(250, 250 / 16 * 9, 7);
        } else if (args.length > 0 && args[0].equals("debug")) {
            canvas = new DebugPixelCanvas(150, 70);
        } else {
            canvas = new ConsolePixelCanvas(150, 70, 2, 8);
        }

        final ScreenManager screenManager = new ScreenManager(canvas);
        screenManager.push(RestartScreen::new);
        screenManager.push(new AttackScreen(canvas, 25, 25));

        for (; ; ) {
            screenManager.render();
            Thread.sleep(1000 / FPS);
        }
    }

}
