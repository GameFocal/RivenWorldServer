package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

public class Location implements Serializable {

    private float x;

    private float y;

    private float z;

    private float[] rotation = new float[]{0f, 0f, 0f};

    private final Vector3 up = new Vector3(0, 1, 0);

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public Location(float x, float y, float z, float[] rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
    }

    public static Location fromString(String gameString) {
        String[] parts = gameString.split(",");
        if (parts.length == 6) {
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

        return null;
    }

    public Location avg(Location... locations) {
        float x = 0;
        float y = 0;
        float z = 0;

        for (Location l : locations) {
            x += l.getX();
            y += l.getY();
            z += l.getZ();
        }

        this.setX(x / locations.length);
        this.setY(y / locations.length);
        this.setZ(z / locations.length);
        return this;
    }

    public static Location fromVector(Vector3 vector3, Vector3 rotation) {
        return new Location(vector3.x, vector3.y, vector3.z, new float[]{rotation.x, rotation.y, rotation.z});
    }

    public static Location fromVector(Vector3 vector3) {
        return new Location(vector3.x, vector3.y, vector3.z, new float[]{0, 0, 0});
    }

    public static Location fromVector(Vector3 vector3, float[] rotation) {
        return new Location(vector3.x, vector3.y, vector3.z, new float[]{rotation[0], rotation[1], rotation[2]});
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

    public Location setRotation(float[] r) {
        this.rotation = r;
        return this;
    }

    public Location setRotation(Vector3 r) {
        this.rotation = new float[]{r.x, r.y, r.z};
        return this;
    }

    public Location setRotation(float x, float y, float z) {
        this.rotation = new float[]{x, y, z};
        return this;
    }

    public float dist(Location loc2) {
        if (loc2 == null)
            return 0;

        return this.toVector().dst(loc2.toVector());
//
//        float xdiff = (loc2.x - this.x);
//        float ydiff = (loc2.y - this.y);
//        return Math.round(Math.floor(Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2))));
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

    public String simpleString() {
        return ((int) Math.floor(this.x)) + "," + (int) Math.floor(this.y) + "," + (int) Math.floor(this.z);
    }

    public void lookAt(Location lookAt) {
        this.lookAt(lookAt.toVector());
    }

    public void lookAt(Vector3 lookAt) {

        float deg = (float) VectorUtil.getDegrees(this.toVector(), lookAt);

//        System.out.println(this.toVector().dot(lookAt));

        Vector3 v = this.toVector().nor();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        Vector3 tmpVec = lookAt.sub(this.toVector()).nor();
        if (!tmpVec.isZero()) {

            float dot = tmpVec.dot(up); // up and direction must ALWAYS be orthonormal vectors
            if (Math.abs(dot - 1) < 0.000000001f) {
                // Collinear
                up.set(this.toVector()).scl(-1);
            } else if (Math.abs(dot + 1) < 0.000000001f) {
                // Collinear opposite
                up.set(this.toVector());
            }

            tmpVec.set(this.toVector()).crs(up);
            up.set(tmpVec).crs(this.toVector()).nor();

            this.setRotation(MathUtils.radiansToDegrees * tmpVec.x, MathUtils.radiansToDegrees * tmpVec.y, MathUtils.radiansToDegrees * tmpVec.z);
        }

        // Z Rot
        this.rotation[2] = deg;
    }
}
