package com.gamefocal.rivenworld.game.player;

public enum Animation {
    SWING_AXE("Anim_swing_axe"),
    FORAGE_GROUND("ForageGround"),
    FORAGE_TREE("ForageTree"),
    TAKE_HIT("standing_react_large_from_right_Retargeted"),
    Digging("Digging"),
    Eat("Eat"),
    PICKAXE("Pickaxe"),
    PUNCH("Punch"),
    GATHER_WATER("GatherWater"),
    Block("Block"),
    Torch("Torch"),
    SpearQuick("SpearQuick"),
    SpearHeavy("SpearHeavy"),
    twoHandQuick("twoHandQuick"),
    twoHandHeavy("twoHandHeavy"),
    oneHandQuick("oneHandQuick"),
    oneHandHeavy("oneHandHeavy")
    ;

    private String unrealName;

    Animation(String unrealName) {
        this.unrealName = unrealName;
    }

    public String getUnrealName() {
        return unrealName;
    }
}
