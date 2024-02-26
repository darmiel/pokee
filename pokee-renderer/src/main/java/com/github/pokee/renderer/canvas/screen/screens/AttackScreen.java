package com.github.pokee.renderer.canvas.screen.screens;

import com.github.pokee.renderer.DrawablePokemon;
import com.github.pokee.renderer.PokemonType;
import com.github.pokee.renderer.canvas.animation.BerzierAnimation;
import com.github.pokee.renderer.canvas.animation.BounceAnimation;
import com.github.pokee.renderer.canvas.animation.DamageAnimation;
import com.github.pokee.renderer.canvas.animation.SnowflakeAnimation;
import com.github.pokee.renderer.canvas.canvas.Canvas;
import com.github.pokee.renderer.canvas.draw.ColorUtils;
import com.github.pokee.renderer.canvas.draw.SpriteType;
import com.github.pokee.renderer.canvas.font.Fonts;
import com.github.pokee.renderer.canvas.screen.Screen;
import com.github.pokee.renderer.canvas.screen.result.PopRenderResult;
import com.github.pokee.renderer.canvas.screen.result.RenderResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AttackScreen extends Screen {

    private final BufferedImage attackerSprite;
    private final BufferedImage defenderSprite;

    private final Color attackerCommonColor;
    private final Color defenderCommonColor;

    private final BounceAnimation defenderBounce = new BounceAnimation(13, 18);
    final BerzierAnimation attackerEnterAnimation;
    final BerzierAnimation defenderEnterAnimation;
    final DamageAnimation damageAnimation = new DamageAnimation(2);
    final BounceAnimation wiggleAnimation = new BounceAnimation(0, 1);
    private final SnowflakeAnimation ttbSnow;
    private final SnowflakeAnimation bttSnow;

    private double attackerHealth;
    private double defenderHealth;

    public AttackScreen(final Canvas canvas, final int attackerID, final int defenderID) throws IOException {
        super(canvas);

        this.attackerHealth = 1.0;
        this.defenderHealth = 1.0;

        final DrawablePokemon attacker = new DrawablePokemon(attackerID, "attacker", PokemonType.ELECTRIC);
        this.attackerSprite = attacker.getSprites()[SpriteType.BACK.ordinal()];

        final DrawablePokemon defender = new DrawablePokemon(defenderID, "defender", PokemonType.FIRE);
        this.defenderSprite = defender.getSprites()[SpriteType.FRONT.ordinal()];

        this.attackerCommonColor = ColorUtils.findMostCommon(this.attackerSprite, Color.YELLOW);
        this.defenderCommonColor = ColorUtils.findMostCommon(this.defenderSprite, Color.ORANGE);

        // animations
        this.attackerEnterAnimation = this.registerAnimation(BerzierAnimation.easeOut(20));
        this.defenderEnterAnimation = this.registerAnimation(BerzierAnimation.easeOut(20));

        this.ttbSnow = this.registerAnimation(new SnowflakeAnimation(
                canvas.getWidth(),
                canvas.getHeight(),
                new int[]{
                        Color.GRAY.getRGB()
                },
                0.0025f
        ));
        this.bttSnow = this.registerAnimation(new SnowflakeAnimation(
                canvas.getWidth(),
                canvas.getHeight(),
                new int[]{
                        Color.DARK_GRAY.getRGB()
                },
                0.0025f
        ));
    }

    @Override
    public RenderResult onDraw(final Canvas canvas) {
        final int attackerWidth = this.attackerSprite.getWidth();
        final int attackerHeight = this.attackerSprite.getHeight();
        final int attackerY = canvas.getHeight() - 80;

        final int defenderWidth = this.defenderSprite.getWidth();
        final int defenderHeight = this.defenderSprite.getHeight();

        final int attackerEnterStart = canvas.getWidth();
        final int attackerEnterEnd = 25;

        final int defenderEnterStart = -defenderWidth;
        final int defenderEnterEnd = canvas.getWidth() - defenderWidth - 25;

        bttSnow.renderReversed(canvas);
        ttbSnow.render(canvas);

        attackerEnterAnimation.tick();
        final int attackerX = attackerEnterAnimation.getValue(attackerEnterStart, attackerEnterEnd);

        defenderEnterAnimation.tick();
        defenderBounce.tick();
        final int defenderX = defenderEnterAnimation.getValue(defenderEnterStart, defenderEnterEnd);

        // for the attacker attack, we need to draw a line from attacker to defender
        // attacker attacks with at least 7 different lines from random positions
        if (defenderEnterAnimation.isDone() && attackerEnterAnimation.isDone()) {
            damageAnimation.tick();
            wiggleAnimation.tick();

            // create 7 different locations from random positions outside of the canvas
            final int[][] attackPositions = new int[14][2];
            for (int i = 0; i < attackPositions.length; i++) {
                final int x = (int) (Math.random() * canvas.getWidth() * 2) - canvas.getWidth();
                final int y = (int) (Math.random() * canvas.getHeight() * 2) - canvas.getHeight();

                final double factor = Math.pow(.9, i);
                final Color attackerColor = ColorUtils.darker(this.attackerCommonColor, factor);
                final Color defenderColor = ColorUtils.darker(this.defenderCommonColor, factor);

                canvas.drawLine(
                        attackerX + attackerWidth / 2,
                        attackerY + attackerHeight / 2,
                        x,
                        y,
                        1,
                        attackerColor.getRGB()
                );
                canvas.drawLine(
                        defenderX + defenderWidth / 2,
                        defenderBounce.getY() + defenderHeight / 2,
                        x,
                        y,
                        1,
                        defenderColor.getRGB()
                );

            }
        }

        // draw defender shadow
        canvas.fillEllipse(
                defenderX + 2 + 13,
                canvas.getHeight() - 85,
                20,
                10,
                Color.DARK_GRAY.getRGB()
        );
        canvas.drawImage(
                defenderX,
                defenderBounce.getY(),
                defenderSprite,
                false,
                damageAnimation.getColor()
        );

        // draw attacker
        canvas.drawImage(
                attackerX + wiggleAnimation.getY(),
                attackerY,
                attackerSprite,
                false
        );

        // Progress Bar attacker
        canvas.fillRect(attackerX + attackerWidth + 10, attackerY + 3, 30, 5, Color.WHITE.getRGB());
        canvas.fillRect(attackerX + attackerWidth + 1 + 10, attackerY + 3 + 1, 28, 3, Color.RED.getRGB());
        canvas.fillRect(attackerX + attackerWidth + 1 + 10, attackerY + 3 + 1, (int) (28 * this.attackerHealth), 3, Color.GREEN.getRGB());

        // Progress Bar defender
        canvas.fillRect(defenderX - 30 - 6, defenderBounce.getY() + 3, 30, 5, Color.WHITE.getRGB());
        canvas.fillRect(defenderX - 30 - 6 + 1, defenderBounce.getY() + 1 + 3, 28, 3, Color.RED.getRGB());
        canvas.fillRect(defenderX - 30 - 6 + 1, defenderBounce.getY() + 1 + 3, (int) (28 * this.defenderHealth), 3, Color.GREEN.getRGB());

        final int textY = attackerY + attackerHeight;
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
                "attacker ",
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
        canvas.drawText(
                textX + previousTextWidth,
                textY + 5,
                "defender",
                Fonts.SMALL,
                Color.RED.getRGB()
        );

        // decrease health
        if (defenderEnterAnimation.isDone() &&
                attackerEnterAnimation.isDone() &&
                ((this.attackerHealth -= 0.03) < 0 || (this.defenderHealth -= 0.03) < 0)) {
            return PopRenderResult.INSTANCE;
        }

        return null;
    }

}
