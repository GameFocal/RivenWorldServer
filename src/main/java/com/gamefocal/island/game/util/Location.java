package com.gamefocal.island.game.util;

import java.io.Serializable;

public class Location implements Serializable {

    private float x;

    private float y;

    private float z;

    private float[] rotation = new float[]{0f, 0f, 0f};

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(float x, float y, float z, float[] rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
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

    public float[] getRotation() {
        return rotation;
    }

    public float dist(Location loc2) {
        if (loc2 == null)
            return 0;

        float xdiff = (loc2.x - this.x);
        float ydiff = (loc2.z - this.z);
        return Math.round(Math.floor(Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2))));
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z + "," + this.rotation[0] + "," + this.rotation[1] + "," + this.rotation[2];
    }
}
