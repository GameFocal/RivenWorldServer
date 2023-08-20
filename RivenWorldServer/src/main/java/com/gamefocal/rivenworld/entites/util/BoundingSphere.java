package com.gamefocal.rivenworld.entites.util;

import com.badlogic.gdx.math.Vector3;

public class BoundingSphere {
    private Vector3 center;
    private float radius;

    public BoundingSphere(Vector3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }
}
