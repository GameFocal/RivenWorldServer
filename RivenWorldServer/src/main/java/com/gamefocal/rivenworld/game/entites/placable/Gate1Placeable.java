package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class Gate1Placeable extends DoorEntity<Gate1Placeable> {

    public Gate1Placeable() {
        this.type = "Gate1Placeable";
        this.initHealth(6000);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 120, 250);
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
}
