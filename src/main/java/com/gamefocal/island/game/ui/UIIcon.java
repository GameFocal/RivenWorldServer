package com.gamefocal.island.game.ui;

public enum UIIcon {

    LOCK("Texture2D'/Game/Assets/2dIcons/LockIcon.LockIcon'"),
    UNLOCK("Texture2D'/Game/Assets/2dIcons/UnlockIcon.UnlockIcon'"),
    PICKUP("Texture2D'/Game/Assets/2dIcons/PickupIcon.PickupIcon'");

    private String path;

    UIIcon(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
