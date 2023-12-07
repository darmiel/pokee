package com.github.pokee;

import com.github.pokee.canvas.animation.BerzierAnimation;
import com.github.pokee.canvas.animation.BounceAnimation;
import com.github.pokee.canvas.animation.DamageAnimation;
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

    public static final int FPS = 20;

    private static void run(final Canvas canvas) throws IOException, InterruptedException {
        final DrawablePokemon pikachu = new DrawablePokemon(25, "Pikachu", PokemonType.ELECTRIC);
        final int pikachuWidth = pikachu.getSprites()[SpriteType.BACK.ordinal()].getWidth();
        final int pikachuHeight = pikachu.getSprites()[SpriteType.BACK.ordinal()].getHeight();
        final int pikachuY = canvas.getHeight() - 80;

        final DrawablePokemon glumanda = new DrawablePokemon(4, "Glumanda", PokemonType.FIRE);
        final int glumandaWidth = glumanda.getSprites()[SpriteType.FRONT.ordinal()].getWidth();
        final int glumandaHeight = glumanda.getSprites()[SpriteType.FRONT.ordinal()].getHeight();
        final BounceAnimation glumandaBounce = new BounceAnimation(13, 18);

        final BerzierAnimation pikachuEnterAnimation = BerzierAnimation.easeOut(20);
        final int pikachuEnterStart = canvas.getWidth();
        final int pikachuEnterEnd = 25;

        final BerzierAnimation glumandaEnterAnimation = BerzierAnimation.easeOut(20);
        final int glumandaEnterStart = -glumandaWidth;
        final int glumandaEnterEnd = canvas.getWidth() - glumandaWidth - 25;

        final DamageAnimation damageAnimation = new DamageAnimation(2);

        final SnowflakeAnimation ttbSnow = new SnowflakeAnimation(
                canvas.getWidth(),
                canvas.getHeight(),
                new int[]{
                        Color.GRAY.getRGB()
//                        Color.ORANGE.darker().darker().darker().darker().getRGB(),
//                        Color.YELLOW.darker().darker().darker().darker().getRGB(),
                },
                0.0025f
        );
        final SnowflakeAnimation bttSnow = new SnowflakeAnimation(
                canvas.getWidth(),
                canvas.getHeight(),
                new int[]{
//                        Color.RED.darker().darker().darker().darker().getRGB(),
//                        Color.ORANGE.darker().darker().darker().darker().getRGB(),
                        Color.DARK_GRAY.getRGB()
                },
                0.0025f
        );


        double health = 1.0;

        while (true) {
            canvas.reset();

            bttSnow.tick();
            bttSnow.renderReversed(canvas);
            ttbSnow.tick();
            ttbSnow.render(canvas);

            pikachuEnterAnimation.tick();
            final int pikachuX = pikachuEnterAnimation.getValue(pikachuEnterStart, pikachuEnterEnd);

            glumandaEnterAnimation.tick();
            glumandaBounce.tick();
            final int glumandaX = glumandaEnterAnimation.getValue(glumandaEnterStart, glumandaEnterEnd);

            // for the pikachu attack, we need to draw a line from pikachu to glumanda
            // pikachu attacks with at least 7 different lines from random positions
            if (glumandaEnterAnimation.isDone() && pikachuEnterAnimation.isDone()) {
                damageAnimation.tick();

                // create 7 different locations from random positions outside of the canvas
                final int[][] attackPositions = new int[7][2];
                for (int i = 0; i < attackPositions.length; i++) {
                    final int x = (int) (Math.random() * canvas.getWidth() * 2) - canvas.getWidth();
                    final int y = (int) (Math.random() * canvas.getHeight() * 2) - canvas.getHeight();

                    Color pikachuColor = Color.YELLOW;
                    Color glumandaColor = Color.ORANGE;
                    for (int i1 = 0; i1 < i; i1++) {
                        pikachuColor = pikachuColor.darker();
                        glumandaColor = glumandaColor.darker();
                    }


                    canvas.drawLine(
                            pikachuX + pikachuWidth / 2,
                            pikachuY + pikachuHeight / 2,
                            x,
                            y,
                            1,
                            pikachuColor.getRGB()
                    );
                    canvas.drawLine(
                            glumandaX + glumandaWidth / 2,
                            glumandaBounce.getY() + glumandaHeight / 2,
                            x,
                            y,
                            1,
                            glumandaColor.getRGB()
                    );

                }
            }

            // draw glumanda

            // draw glumanda shadow
            canvas.fillEllipse(
                    glumandaX + 2 + 13,
                    canvas.getHeight() - 85,
                    20,
                    10,
                    Color.DARK_GRAY.getRGB()
            );
            canvas.drawImage(
                    glumandaX,
                    glumandaBounce.getY(),
                    glumanda.getSprites()[SpriteType.FRONT.ordinal()],
                    false,
                    damageAnimation.getColor()
            );

            // draw pikachu
            canvas.drawImage(
                    pikachuX,
                    pikachuY,
                    pikachu.getSprites()[SpriteType.BACK.ordinal()],
                    false
            );

            // Progress Bar Pikachu
            canvas.fillRect(pikachuX + pikachuWidth + 10, pikachuY + 3, 30, 5, Color.WHITE.getRGB());
            canvas.fillRect(pikachuX + pikachuWidth + 1 + 10, pikachuY + 3 + 1, 28, 3, Color.RED.getRGB());
            canvas.fillRect(pikachuX + pikachuWidth + 1 + 10, pikachuY + 3 + 1, (int) (28 * health), 3, Color.GREEN.getRGB());

            // Progress Bar Glumanda
            canvas.fillRect(glumandaX - 30 - 6, glumandaBounce.getY() + 3, 30, 5, Color.WHITE.getRGB());
            canvas.fillRect(glumandaX - 30 - 6 + 1, glumandaBounce.getY() + 1 + 3, 28, 3, Color.RED.getRGB());
            canvas.fillRect(glumandaX - 30 - 6 + 1, glumandaBounce.getY() + 1 + 3, (int) (28 * health), 3, Color.GREEN.getRGB());

            final int textY = pikachuY + pikachuHeight;
            // print text box
            canvas.fillRect(
                    5,
                    textY,
                    canvas.getWidth() - 10,
                    13,
                    Color.DARK_GRAY.getRGB()
            );

            final int textX = 7;
            int previousTextWidth = canvas.drawText(
                    textX,
                    textY + 5,
                    "Now Playing: ",
                    Fonts.SMALL,
                    Color.WHITE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    textY + 5,
                    "Pikachu ",
                    Fonts.SMALL,
                    Color.ORANGE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    textY + 5,
                    "vs. ",
                    Fonts.SMALL,
                    Color.WHITE.getRGB()
            );
            previousTextWidth += canvas.drawText(
                    textX + previousTextWidth,
                    textY + 5,
                    "Glumanda",
                    Fonts.SMALL,
                    Color.RED.getRGB()
            );


//            canvas.drawLine(10, 10, canvas.getWidth() - 10, canvas.getHeight() - 10, 1, Color.RED.getRGB());
//            canvas.drawLine(10, canvas.getHeight() - 10, canvas.getWidth() - 10, 10, 1, Color.RED.getRGB());

            ((Renderable<?>) canvas).render();

            Thread.sleep(1000 / FPS);

            // decrease health
            if (glumandaEnterAnimation.isDone() && pikachuEnterAnimation.isDone() && (health -= 0.03) < 0) {
                break;
            }
        }

        canvas.reset();
        canvas.fillRect(0, 0, canvas.getWidth(), canvas.getHeight(), Color.RED.getRGB());
        ((Renderable<?>) canvas).render();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final Canvas canvas;
        if (args.length > 0 && args[0].equals("frame")) {
            canvas = new JFramePixelCanvas(250, 250 / 16 * 9, 2);
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
