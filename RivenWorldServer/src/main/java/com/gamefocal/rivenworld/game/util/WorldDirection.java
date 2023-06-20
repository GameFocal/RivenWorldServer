package com.gamefocal.rivenworld.game.util;

public enum WorldDirection {

    WEST,
    SOUTH_WEST,
    SOUTH,
    SOUTH_EAST,
    EAST,
    NORTH_EAST,
    NORTH,
    NORTH_WEST;

    public static WorldDirection getDirection(double bearing) {
        int index = ((int) Math.round(((bearing % 360) / 45)) % 8);
        return WorldDirection.values()[index];
    }

}
