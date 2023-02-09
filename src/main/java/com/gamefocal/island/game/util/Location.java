package com.gamefocal.island.game.util;

import com.badlogic.gdx.math.Vector3;
import org.apache.commons.codec.digest.DigestUtils;

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

    public static Location fromString(String gameString) {
        String[] parts = gameString.split(",");
        return new Location(
                Float.parseFloat(parts[0]),
                Float.parseFloat(parts[1]),
                Float.parseFloat(parts[2]),
                new float[]{
                        Float.parseFloat(parts[3]),
                        Float.parseFloat(parts[4]),
                        Float.parseFloat(parts[5])
                }
        );
    }

    public String getHash() {
        return DigestUtils.md5Hex(this.toString());
    }

    public float getX() {
        return x;
    }

    public Location setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public Location setY(float y) {
        this.y = y;
        return this;
    }

    public float getZ() {
        return z;
    }

    public Location setZ(float z) {
        this.z = z;
        return this;
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

    public Location addX(float a) {
        this.x += a;
        return this;
    }

    public Location addY(float a) {
        this.y += a;
        return this;
    }

    public Location addZ(float a) {
        this.z += a;
        return this;
    }

    @Override
    public String toString() {
        return (this.x + "," + this.y + "," + this.z + "," + this.rotation[0] + "," + this.rotation[1] + "," + this.rotation[2]);
    }

    public Location cpy() {
        return new Location(this.x, this.y, this.z, new float[]{this.rotation[0], this.rotation[1], this.rotation[2]});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && Location.class.isAssignableFrom(obj.getClass())) {
            return obj.toString().equalsIgnoreCase(this.toString());
        }
        return false;
    }

    public Vector3 toVector() {
        return new Vector3(this.x, this.y, this.z);
    }
}
