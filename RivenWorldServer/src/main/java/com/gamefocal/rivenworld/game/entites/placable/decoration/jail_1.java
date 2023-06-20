package com.gamefocal.rivenworld.game.entites.placable.decoration;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class jail_1 extends DoorEntity<jail_1> {

    public jail_1() {
        this.type = "jail_1";
        this.health = 2000;
        this.maxHealth = 2000;
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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 100, 200);
    }
}
