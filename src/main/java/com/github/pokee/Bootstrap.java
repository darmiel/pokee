package com.github.pokee;

import com.github.pokee.canvas.BounceAnimation;
import com.github.pokee.canvas.StringCanvas;
import com.github.pokee.canvas.draw.SpriteType;
import com.github.pokee.canvas.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class Bootstrap {

    private static SpriteType getTypeForX(final int x) {
        if (x <= 25) {
            return SpriteType.BACK;
        }
        return SpriteType.FRONT;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final StringCanvas canvas = new StringCanvas(150, 70);

        final DrawablePokemon pikachu = new DrawablePokemon(25, "Pikachu", PokemonType.ELECTRIC);
        final BounceAnimation pikachuBounce = new BounceAnimation(0, 3);

        final DrawablePokemon glumanda = new DrawablePokemon(4, "Glumanda", PokemonType.FIRE);
        final BounceAnimation glumandaBounce = new BounceAnimation(-5, -2);

        final int padding = 5;
        final BounceAnimation panAnimation = new BounceAnimation(-5, 60);

        double health = 1.0;

        while (true) {
            canvas.reset();

            final int pikachuX = panAnimation.getY();
            final SpriteType pikachuType = getTypeForX(pikachuX);
            final Runnable pikachuDraw = () -> canvas.drawImage(
                    pikachuX,
                    pikachuBounce.getY(),
                    pikachu.getSprites()[pikachuType.ordinal()],
                    false
            );

            final int glumandaX = panAnimation.getEndY() - panAnimation.getY() - padding;
            final SpriteType glumandaType = getTypeForX(glumandaX);
            final Runnable glumandaDraw = () -> canvas.drawImage(
                    glumandaX,
                    glumandaBounce.getY(),
                    glumanda.getSprites()[glumandaType.ordinal()],
                    false
            );

            if (pikachuType == SpriteType.BACK) {
                glumandaDraw.run();
                pikachuDraw.run();
            } else {
                pikachuDraw.run();
                glumandaDraw.run();
            }

            canvas.drawRect(2, 2, 30, 5, Color.BLACK.getRGB());
            canvas.drawRect(3, 3, 28, 3, Color.RED.getRGB());
            canvas.drawRect(3, 3, (int) (28 * health), 3, Color.GREEN.getRGB());

            // print text box
            canvas.drawRect(5, canvas.getHeight() - 5 - 8, canvas.getWidth() - 10, 8, Color.WHITE.getRGB());

            int previousTextWidth = canvas.drawText(
                    7,
                    canvas.getHeight() - 5 - 7,
                    "Hallo.Ich bin der: ",
                    Fonts.SMALL,
                    Color.BLACK.getRGB()
            );
            previousTextWidth = canvas.drawText(
                    7 + previousTextWidth,
                    canvas.getHeight() - 5 - 7,
                    "Daniel",
                    Fonts.SMALL,
                    Color.RED.getRGB()
            );

            canvas.drawLine(10, 10, canvas.getWidth() - 10, canvas.getHeight() - 10, 1, Color.RED.getRGB());
            canvas.drawLine(10, canvas.getHeight() - 10, canvas.getWidth() - 10, 10, 1, Color.RED.getRGB());

            System.out.println("\n\n\n\n\n" + canvas.render(2));

            pikachuBounce.tick();
            glumandaBounce.tick();
            panAnimation.tick();

            Thread.sleep(100);

            health -= 0.01;
            if (health < 0) {
                break;
            }
        }

        canvas.reset();
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), Color.RED.getRGB());
        System.out.println(canvas.render(2));
    }

}
