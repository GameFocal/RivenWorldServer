package com.gamefocal.rivenworld.entites.combat;

public enum CombatStance {

    NOT_READY,
    ATTACK_READY,
    ATTACK_FIRE,
    BLOCK;

    public static CombatStance getFromIndex(int i) {
        return CombatStance.values()[i];
    }

}
