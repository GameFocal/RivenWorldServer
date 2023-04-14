package com.gamefocal.rivenworld.game.entites.placable.decoration;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class ChairPlaceable extends PlaceableEntity<ChairPlaceable> {

    public ChairPlaceable() {
        this.type = "ChairPlaceable";
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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }
}
