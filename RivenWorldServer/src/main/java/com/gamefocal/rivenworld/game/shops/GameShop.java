package com.gamefocal.rivenworld.game.shops;

public enum GameShop {
    GENERAL_STORE("gs");

    private String uid;

    GameShop(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
