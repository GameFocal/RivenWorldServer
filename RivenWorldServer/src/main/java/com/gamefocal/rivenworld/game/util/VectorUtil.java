package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class VectorUtil {

    public static Vector3 returnPosAroundObj(Vector3 posObject, Float angleDegrees, Float radius, Float height) {
        float angleRadians = angleDegrees * MathUtils.degreesToRadians;
        Vector3 position = new Vector3();
        position.set(radius * MathUtils.sin(angleRadians), height, radius * MathUtils.cos(angleRadians));
        position.add(posObject); //add the position so it would be arround object
        return position;
    }

    public static Vector3 rot(Vector3 src, float u, float w, float v, float theta) {
        float x = src.x;
        float y = src.y;
        float z = src.z;

        double xPrime = u * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta);
        double yPrime = v * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta);
        double zPrime = w * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta);

        return new Vector3((float) xPrime, (float) zPrime, (float) yPrime);
    }

    public static Vector3 calculateOrbit(float currentOrbitDegrees, float distanceFromCenterPoint, Vector3 centerPoint, Vector3 forward, float height) {

        float radians = (float) Math.toRadians(currentOrbitDegrees);

        float x = (float) ((Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x);
        float y = (float) ((Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y);

        return new Vector3(x, y, height);
    }

    public static double getDegrees(Vector3 start, Vector3 end) {
//        return (float) ((Math.atan2(start.x - end.x, -(start.y - end.y)) * 180.0d / Math.PI));
        return Math.atan2(
                end.y - start.y,
                end.x - start.x
        ) * 180.0d / Math.PI;
    }

    public static double getDegrees(Vector2 start, Vector2 end) {
//        return (float) ((Math.atan2(start.x - end.x, -(start.y - end.y)) * 180.0d / Math.PI));
        return Math.atan2(
                end.y - start.y,
                end.x - start.x
        ) * 180.0d / Math.PI;
    }

    public static float getRadians(Vector3 start, Vector3 end) {
        return (float) ((Math.atan2(start.x - end.x, -(start.y - end.y))));
    }

    public static double getBearing(Vector3 current, Vector2 destination) {
        float angle = (float) Math.toDegrees(Math.atan2(destination.x - current.x, destination.y - current.y));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public static Vector2 getVector2(Vector3 vector3) {
        return new Vector2(vector3.x, vector3.y);
    }

    public static Vector3 fromYawPitch(float yaw, float pitch) {
        float yawRadians = FloatMath.toRadians(yaw);
        float pitchRadians = FloatMath.toRadians(pitch);
        float xz = FloatMath.cos(pitchRadians);
        return new Vector3(-xz * FloatMath.sin(yawRadians), xz * FloatMath.cos(yawRadians), -FloatMath.sin(pitchRadians));
    }

}
