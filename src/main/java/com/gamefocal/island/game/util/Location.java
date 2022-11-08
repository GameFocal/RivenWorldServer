package com.gamefocal.island.game.util;

import java.io.Serializable;

public class Location implements Serializable {

    private float x;

    private float y;

    private float z;

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }
}
