package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.util.BoundingSphere;

public class ShapeUtil {

    public static BoundingBox makeBoundBox(Vector3 baseLocation, float radius, float height) {

        BoundingBox boundingBox = new BoundingBox();
        boundingBox.set(new Vector3[]{
                baseLocation,
                baseLocation.cpy().add(radius, 0, -(height / 2)),
                baseLocation.cpy().add(-radius, 0, -(height / 2)),
                baseLocation.cpy().add(0, radius, -(height / 2)),
                baseLocation.cpy().add(0, -radius, -(height / 2)),
                baseLocation.cpy().add(0, 0, (height / 2)),
                baseLocation.cpy().add(radius, 0, (height / 2)),
                baseLocation.cpy().add(-radius, 0, (height / 2)),
                baseLocation.cpy().add(0, radius, (height / 2)),
                baseLocation.cpy().add(0, -radius, (height / 2)),
        });

        return boundingBox;
    }

    public static BoundingBox createBoundingBoxFromLocations(Location loc1, Location loc2) {
        float minX = Math.min(loc1.getX(), loc2.getX());
        float minY = Math.min(loc1.getY(), loc2.getY());
        float minZ = Math.min(loc1.getZ(), loc2.getZ());

        float maxX = Math.max(loc1.getX(), loc2.getX());
        float maxY = Math.max(loc1.getY(), loc2.getY());
        float maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new BoundingBox(new Vector3(minX, minY, minZ), new Vector3(maxX, maxY, maxZ));
    }

    public static boolean isLocationInsideWithTolerance(BoundingBox box, Vector3 location, float tolerance) {
        BoundingBox expandedBox = new BoundingBox(
                new Vector3(box.min.x - tolerance, box.min.y - tolerance, box.min.z - tolerance),
                new Vector3(box.max.x + tolerance, box.max.y + tolerance, box.max.z + tolerance)
        );

        return expandedBox.contains(location);
    }

    public static Rectangle boundingBoxToRectangle(BoundingBox box) {
        float minX = box.min.x;
        float minY = box.min.y;

        float maxX = box.max.x;
        float maxY = box.max.y;

        float width = maxX - minX;
        float height = maxY - minY;

        return new Rectangle(minX, minY, width, height);
    }

    public static BoundingSphere computeBoundingSphere(Location point1, Location point2) {
        // Calculate center of the box
        float centerX = (point1.getX() + point2.getX()) / 2.0f;
        float centerY = (point1.getY() + point2.getY()) / 2.0f;
        float centerZ = (point1.getZ() + point2.getZ()) / 2.0f;
        Vector3 center = new Vector3(centerX, centerY, centerZ);

        // Calculate dimensions of the box
        float width = Math.abs(point1.getX() - point2.getX());
        float height = Math.abs(point1.getY() - point2.getY());
        float depth = Math.abs(point1.getZ() - point2.getZ());

        // The radius would be half of the largest dimension of the box
        float radius = Math.max(Math.max(width, height), depth) / 2.0f;

        return new BoundingSphere(center, radius);
    }

}
