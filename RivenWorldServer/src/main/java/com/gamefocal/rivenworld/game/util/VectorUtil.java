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

}
