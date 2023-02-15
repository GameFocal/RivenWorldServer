package com.gamefocal.island.entites.combat;

public enum CombatAngle {

    UPPER,
    FORWARD,
    LEFT,
    RIGHT;

    public static CombatAngle getFromIndex(int i) {
        return CombatAngle.values()[i];
    }

}
