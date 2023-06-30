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

    public static int getIndex(WorldDirection direction) {

        int i = 0;

        for (WorldDirection d : values()) {
            if (d == direction) {
                return i;
            }
            i++;
        }

        return 0;
    }

    // get the direction to the left of the current direction
    public WorldDirection getLeftDirection() {
        int currentIndex = getIndex(this);
        int leftIndex = (currentIndex + 7) % 8; // subtract 1, but add 7 to avoid negative result
        return WorldDirection.values()[leftIndex];
    }

    // get the direction to the right of the current direction
    public WorldDirection getRightDirection() {
        int currentIndex = getIndex(this);
        int rightIndex = (currentIndex + 1) % 8; // add 1, mod 8 to wrap around if necessary
        return WorldDirection.values()[rightIndex];
    }

    public WorldDirection getOppositeDirection() {
        int index = getIndex(this);
        return WorldDirection.values()[(index + 4) % 8];
    }

}
