package com.gamefocal.rivenworld.game.entites.placable.decoration;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class TablePlaceable extends PlaceableEntity<TablePlaceable> {

    public TablePlaceable() {
        this.type = "TablePlaceable";
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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 50);
    }
}
