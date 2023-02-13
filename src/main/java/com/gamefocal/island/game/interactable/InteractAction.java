package com.gamefocal.island.game.interactable;

import com.gamefocal.island.game.util.Location;

public enum InteractAction {

    HIT,
    PRIMARY,
    SECONDARY,
    ALT,
    CONSUME,
    USE,
    HIT_DISTANCE,
    TOGGLE_ON_OFF,
    RADIAL_MENU,
    RADIAL_SELECTION;

    private Location interactLocation = null;

    public Location getInteractLocation() {
        return interactLocation;
    }

    public InteractAction setLocation(Location interactLocation) {
        this.interactLocation = interactLocation;
        return this;
    }
}
