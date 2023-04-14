package com.gamefocal.rivenworld.game.entites.placable.decoration;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class BedPlaceable extends PlaceableEntity<BedPlaceable> {

    public BedPlaceable() {
        this.type = "bedPlaceable";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 75, 50);
    }
}
