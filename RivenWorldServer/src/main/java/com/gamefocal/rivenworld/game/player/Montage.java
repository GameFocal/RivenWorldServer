package com.gamefocal.rivenworld.game.player;

public enum Montage {
    OneHandCombo("OneHandCombo"),
    TwoHandCombo("TwoHandCombo"),
    ;

    private String unrealName;

    Montage(String unrealName) {
        this.unrealName = unrealName;
    }

    public String getUnrealName() {
        return unrealName;
    }
}
