package com.gamefocal.island.game.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

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

}
