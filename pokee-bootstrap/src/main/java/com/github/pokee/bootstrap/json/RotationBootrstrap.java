package com.github.pokee.bootstrap.json;

import com.github.pokee.canvas.Canvas;
import com.github.pokee.canvas.PixelCanvas;
import com.github.pokee.canvas.util.Rotation;
import com.github.pokee.canvas.screen.Screen;
import com.github.pokee.canvas.screen.ScreenManager;
import com.github.pokee.canvas.screen.result.RenderResult;
import com.github.pokee.canvas.util.console.ConsolePixelCanvas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RotationBootrstrap {

    public static final int FPS = 12;

    public static class Point2D {
        public double x, y;
    }


    public final static class Box {


        int xRotation = 0;
        int yRotation = 0;
        int zRotation = 0;

        public static final BufferedImage image;

        static {
            try {
                image = ImageIO.read(new File("pokeapi-sprites/sprites/pokemon/2.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public RenderResult render(final Canvas canvas, final Point2D cursor) {

            // use a temporary buffer for drawing
            final int padding = 15;
            final PixelCanvas buffer = new PixelCanvas(2 * padding + image.getWidth(), 2 * padding + image.getHeight());
            buffer.drawImage(padding, padding, image, true);

            final int anchorX = padding + (buffer.getWidth() - padding - padding) / 2;
            final int anchorY = padding + (buffer.getHeight() - padding - padding) / 2;

            final int[][] colors = buffer.getColors();
            final int[][] newPixels = new int[buffer.getHeight()][buffer.getWidth()];
            for (int y = 0; y < colors.length; y++) {
                for (int x = 0; x < colors[y].length; x++) {
                    final double[] newPoint = Rotation.rotatePoint(
                            new double[]{0, x - anchorX, y - anchorY},
                            Math.toRadians(this.xRotation),
                            Math.toRadians(this.yRotation),
                            Math.toRadians(this.zRotation)
                    );
                    final int newX = (int) newPoint[1] + anchorX;
                    final int newY = (int) newPoint[2] + anchorY;
                    canvas.drawPixel(newY, newX, colors[y][x]);
                }
            }


            // rotate all points
            xRotation += 2;
            yRotation +=2;
            zRotation += 2;

            if (yRotation == 360) {
                yRotation += 2;
            }
            if (zRotation == 360) {
                zRotation += 2;
            }

            System.out.println(this.zRotation);

            return null;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final Point2D cursor = new Point2D();
        final ConsolePixelCanvas canvas = new ConsolePixelCanvas(80, 80, 2, 10);

        final ScreenManager screenManager = new ScreenManager(canvas);

        final Box box = new Box();

        screenManager.push(new Screen(canvas) {
            @Override
            public RenderResult onDraw(final Canvas canvas) {
                return box.render(canvas, cursor);
            }
        });

        //noinspection InfiniteLoopStatement
        for (; ; ) {
            screenManager.render();
            //noinspection BusyWait
            Thread.sleep(1000 / FPS);
        }
    }

}
