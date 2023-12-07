package com.github.pokee;

import com.github.pokee.canvas.animation.BerzierAnimation;
import com.github.pokee.canvas.animation.BounceAnimation;
import com.github.pokee.canvas.animation.SnowflakeAnimation;
import com.github.pokee.canvas.canvas.Canvas;
import com.github.pokee.canvas.display.ConsolePixelCanvas;
import com.github.pokee.canvas.display.JFramePixelCanvas;
import com.github.pokee.canvas.display.Renderable;
import com.github.pokee.canvas.draw.SpriteType;
import com.github.pokee.canvas.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class Bootstrap {

    private static void run(final Canvas canvas) throws IOException, InterruptedException {
        final DrawablePokemon pikachu = new DrawablePokemon(25, "Pikachu", PokemonType.ELECTRIC);
        final BounceAnimation pikachuBounce = new BounceAnimation(3, 6);

        final DrawablePokemon glumanda = new DrawablePokemon(4, "Glumanda", PokemonType.FIRE);
        final BounceAnimation glumandaBounce = new BounceAnimation(0, 3);

        final BerzierAnimation pikachuEnterAnimation = BerzierAnimation.easeOut(20);
        final int pikachuEnterStart = canvas.getWidth();
        final int pikachuEnterEnd = 0;

        final BerzierAnimation glumandaEnterAnimation = BerzierAnimation.easeOut(20);
        final int glumandaWidth = glumanda.getSprites()[SpriteType.FRONT.ordinal()].getWidth();
        final int glumandaEnterStart = -glumandaWidth;
        final int glumandaEnterEnd = canvas.getWidth() - glumandaWidth - 7;

        final SnowflakeAnimation snowflakeAnimation = new SnowflakeAnimation(
                canvas.getWidth(),
                canvas.getHeight(),
                new int[]{
                        Color.RED.darker().darker().darker().darker().getRGB(),
                        Color.ORANGE.darker().darker().darker().darker().getRGB(),
                        Color.YELLOW.darker().darker().darker().darker().getRGB(),
                }
        );

        double health = 1.0;

        while (true) {
            canvas.reset();

            if (pikachuEnterAnimation.isDone() && glumandaEnterAnimation.isDone()) {
                snowflakeAnimation.tick();
                snowflakeAnimation.renderReversed(canvas);
            }

            // draw glumanda
            glumandaEnterAnimation.tick();
            glumandaBounce.tick();
            final int glumandaX = glumandaEnterAnimation.getValue(glumandaEnterStart, glumandaEnterEnd);
            canvas.drawImage(
                    glumandaX,
                    glumandaBounce.getY(),
                    glumanda.getSprites()[SpriteType.FRONT.ordinal()],
                    false
            );

            // draw pikachu
            pikachuEnterAnimation.tick();
            pikachuBounce.tick();
            final int pikachuX = pikachuEnterAnimation.getValue(pikachuEnterStart, pikachuEnterEnd);
            canvas.drawImage(
                    pikachuX,
                    pikachuBounce.getY(),
                    pikachu.getSprites()[SpriteType.BACK.ordinal()],
                    false
            );

            // Progress Bar
            canvas.drawRect(2, 2, 30, 5, Color.BLACK.getRGB());
            canvas.drawRect(3, 3, 28, 3, Color.RED.getRGB());
            canvas.drawRect(3, 3, (int) (28 * health), 3, Color.GREEN.getRGB());

            // print text box
            canvas.drawRect(
                    5,
                    canvas.getHeight() - 5 - 8,
                    canvas.getWidth() - 10,
                    8,
                    Color.DARK_GRAY.getRGB()
            );

            final int textX = 7;
            int previousTextWidth = canvas.drawText(
                    textX,
                    canvas.getHeight() - 5 - 5,
                    "Now Playing: ",
                    Fonts.SMALL,
                    Color.WHITE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    canvas.getHeight() - 5 - 5,
                    "Pikachu ",
                    Fonts.SMALL,
                    Color.ORANGE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    canvas.getHeight() - 5 - 5,
                    "vs. ",
                    Fonts.SMALL,
                    Color.WHITE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    canvas.getHeight() - 5 - 5,
                    "Glumanda",
                    Fonts.SMALL,
                    Color.RED.getRGB()
            );

//            canvas.drawLine(10, 10, canvas.getWidth() - 10, canvas.getHeight() - 10, 1, Color.RED.getRGB());
//            canvas.drawLine(10, canvas.getHeight() - 10, canvas.getWidth() - 10, 10, 1, Color.RED.getRGB());

            ((Renderable<?>) canvas).render();

            Thread.sleep(100);

            // decrease health
            if (glumandaEnterAnimation.isDone() && pikachuEnterAnimation.isDone() && (health -= 0.03) < 0) {
                break;
            }
        }

        canvas.reset();
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), Color.RED.getRGB());
        ((Renderable<?>) canvas).render();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final Canvas canvas;
        if (args.length > 0 && args[0].equals("frame")) {
            canvas = new JFramePixelCanvas(150, 70, 4);
        } else {
            canvas = new ConsolePixelCanvas(150, 70, 2, 8);
        }
        while (true) {
            Bootstrap.run(canvas);

            int count = 3;
            while (count > 0) {
                canvas.reset();

                final String text = "Restart in " + count + "...";
                final int width = Fonts.SMALL.getWidth(text);
                final int xStart = canvas.getWidth() / 2 - width / 2;

                int restartInWidth = canvas.drawText(
                        xStart,
                        canvas.getHeight() / 2 - Fonts.SMALL.getGlyphHeight(),
                        "Restart in ",
                        Fonts.SMALL,
                        Color.WHITE.getRGB()
                );
                restartInWidth += canvas.drawText(
                        xStart + restartInWidth,
                        canvas.getHeight() / 2 - Fonts.SMALL.getGlyphHeight(),
                        count + " ",
                        Fonts.SMALL,
                        Color.RED.getRGB()
                );
                canvas.drawText(
                        xStart + restartInWidth,
                        canvas.getHeight() / 2 - Fonts.SMALL.getGlyphHeight(),
                        "...",
                        Fonts.SMALL,
                        Color.WHITE.getRGB()
                );


                ((Renderable<?>) canvas).render();
                Thread.sleep(1000);
                count--;
            }
        }
    }

}
