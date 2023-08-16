package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.Vector3;

import java.awt.geom.Point2D;

public class RectangleProperties {
    private Vector3 center;
    private float width;
    private float height;
    private float rotation;

    public RectangleProperties(Vector3 center, float width, float height, float rotation) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotation() {
        return rotation;
    }

    public static RectangleProperties computeProperties(Vector3 point1, Vector3 point2) {
        // Calculate center of the rectangle
        float centerX = (point1.x + point2.x) / 2.0f;
        float centerY = (point1.y + point2.y) / 2.0f;

        // Calculate width and height
        float width = Math.abs(point1.x - point2.x);
        float height = Math.abs(point1.y - point2.y);

        // Calculate rotation
        float deltaY = point2.y - point1.y;
        float deltaX = point2.x - point1.x;
        float rotation = computeZAxisRotation(point1, point2);

//        if (rotation < 0) {
//            rotation += 360;
//        }

        return new RectangleProperties(new Vector3(centerX, centerY, 0), width, height, rotation);
    }

    public static float computeZAxisRotation(Vector3 pointA, Vector3 pointB) {
        Vector3 direction = new Vector3(pointB.x - pointA.x, pointB.y - pointA.y, 0).nor();

        // Reference forward direction
        Vector3 forward = new Vector3(0, 1, 0);

        // Compute dot product and determinant for atan2
        float dot = direction.x * forward.x + direction.y * forward.y;
        float det = direction.x * forward.y - direction.y * forward.x;

        // Compute the angle (in radians)
        float angle = (float) Math.atan2(det, dot);

        // Convert to degrees because Unreal uses degrees for rotation
        return (float) Math.toDegrees(angle);
    }
}
