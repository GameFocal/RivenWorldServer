package com.gamefocal.rivenworld.game.player;

public enum Animation {
    SWING_AXE("Anim_swing_axe"),
    FORAGE_GROUND("ForageGround"),
    FORAGE_TREE("ForageTree"),
    TAKE_HIT("standing_react_large_from_right_Retargeted"),
    Digging("Digging"),
    Eat("Eat"),
    PICKAXE("Pickaxe")
    ;

    private String unrealName;

    Animation(String unrealName) {
        this.unrealName = unrealName;
    }

    public String getUnrealName() {
        return unrealName;
    }
}
