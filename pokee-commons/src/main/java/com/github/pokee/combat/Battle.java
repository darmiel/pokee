package com.github.pokee.combat;

import com.github.pokee.pokee.Pokee;

/**
 * Battle represents the battle between two Pokee
 */
public class Battle {
    private final Pokee mon1;
    private final Pokee mon2;
    private long turnNumber;

    public Battle(Pokee mon1, Pokee mon2) {
        this.mon1 = mon1;
        this.mon2 = mon2;
        this.turnNumber = 1;
    }

    public boolean hasNextTurn() {
        return mon1.getHp() > 0 && mon2.getHp() > 0;
    }

    /**
     *
     * @param attack is the attack that the attacking Pokee should execute
     * @return false if battle is over, otherwise true
     */
    public boolean nextTurn(Attacks attack) {
        Pokee attackingMon;
        Pokee defendingMon;

        if (!hasNextTurn()) {
            return false;
        }

        if (turnNumber % 2 == 0) {
            attackingMon = this.mon1;
            defendingMon = this.mon2;
        } else {
            attackingMon = this.mon2;
            defendingMon = this.mon1;
        }

        AttackMessage ret;
        do {
            ret = attackingMon.attack(defendingMon,attack);
        } while (ret == AttackMessage.DOESNTKNOW);

        turnNumber++;
        return true;
    }
}
