package com.github.pokee.combat;

/**
 * AttackMessage are the different possible Messages from an attacking Turn
 */
public enum AttackMessage {
    MISSED("missed the attack"),
    NOTENOUGHPP("does not have enough PP for this attack"),
    DOESNTKNOW("does not know this attack"),
    ATTACKED("attacked");

    private final String message;
    AttackMessage(String message) {
        this.message = message;
    }
}
