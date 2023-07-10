package com.gamefocal.rivenworld.game.entites.placable.doors;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class DoorPlaceable2 extends DoorEntity<DoorPlaceable2> {

    public DoorPlaceable2() {
        this.type = "DoorPlaceable2";
        this.initHealth(1000);
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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 100);
    }
}
