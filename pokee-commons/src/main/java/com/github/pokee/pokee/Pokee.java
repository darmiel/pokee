package com.github.pokee.pokee;

import com.github.pokee.combat.AttackMessage;
import com.github.pokee.combat.Attacks;

import java.util.List;

/**
 * Pokee is the representation of a creature that is able to battle
 */
public class Pokee {
    final double LEVEL_HP_SCALE = 1.1;
    private final Breeds breed;
    private List<Attacks> attacks;
    private int hp;

    private int level;

    private long pp;
    private long maxPp;

    public Pokee(Breeds breed, List<Attacks> attacks, int level, int maxPp) {
        this.breed = breed;
        this.attacks = attacks;
        this.setMaxPp(maxPp);
        this.setPp(this.getMaxPp());
        this.setHp(this.getMaxHp());
    }

    /**
     *
     * @param attack that should be checked if it is known
     * @return true if the Pokee knows the attack, false otherwise
     */
    private boolean knowsAttack(Attacks attack) {
        return attacks.contains(attack);
    }

    /**
     *
     * @param enemy
     * @param attack
     * @return AttackMessage of the result from the attack
     */
    public AttackMessage attack(Pokee enemy, Attacks attack) {
        if (!this.knowsAttack(attack)) {
            return AttackMessage.DOESNTKNOW;
        }

        if (attack.getPpCost() > this.getPp()) {
            return AttackMessage.NOTENOUGHPP;
        }
        this.setPp(this.getPp() - attack.getPpCost());

        if (Math.random() < attack.getAccuracy()) {
            return AttackMessage.MISSED;
        }

        int damage = (int) (attack.getType().getMultiplierAgainst(enemy.breed.getType()) * (attack.getStrength() / 10));
        enemy.setHp(enemy.getHp() - damage);

        return AttackMessage.ATTACKED;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(hp, 0);
    }

    public int getMaxHp() {
        return (int) (100 + Math.pow(LEVEL_HP_SCALE, getLevel()));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getPp() {
        return pp;
    }

    public void setPp(long pp) {
        this.pp = Math.max(Math.min(pp, getMaxPp()), 0);
    }

    public long getMaxPp() {
        return maxPp;
    }

    public void setMaxPp(long maxPp) {
        this.maxPp = maxPp;
    }
}
