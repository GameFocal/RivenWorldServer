package com.gamefocal.island.game.enviroment.player;

import com.gamefocal.island.entites.util.UnrealEnum;
import fr.devnied.bitlib.BytesUtils;

public enum PlayerDataState implements UnrealEnum<PlayerDataState> {
    VERY_COLD,
    COLD,
    NORMAL,
    HOT,
    VERY_HOT,
    HEALTHY,
    SICK,
    POISONED,
    WOUNDED,
    FATIGUED,
    DRUNK,
    INFECTED,
    BLEEDING,
    WET,
    ON_FIRE,
    OVERWEIGHT,
    HUNGRY,
    THIRSTY,
    LOW_ENERGY,
    LOW_HEALTH,
    LOW_O2;

    private float ticks = -1f;

    PlayerDataState() {
    }

    PlayerDataState(float ticks) {
        this.ticks = ticks;
    }

    public float getTicks() {
        return ticks;
    }

    @Override
    public String getByte() {
        byte b = 0;
        for (PlayerDataState d : values()) {
            if (this == d) {
                return BytesUtils.bytesToString(new byte[]{b});
            }
            b++;
        }

        return "0x00";
    }
}
