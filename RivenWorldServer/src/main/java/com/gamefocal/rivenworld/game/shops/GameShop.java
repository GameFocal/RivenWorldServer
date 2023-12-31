package com.gamefocal.rivenworld.game.shops;

public enum GameShop {
    GENERAL_STORE("gs"),
    CLOTHING_STORE("cloth"),
    FOOD_STORE("food"),
    BLACKSMITH_STORE("smith"),
    LUMBER_STORE("lumber"),
    FARMING("farm");

    private String uid;

    GameShop(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
