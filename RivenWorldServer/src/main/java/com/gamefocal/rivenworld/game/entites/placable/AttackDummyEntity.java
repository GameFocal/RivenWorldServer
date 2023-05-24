package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.Target_Item;

public class AttackDummyEntity extends DestructibleEntity<Target_Item> {

    public AttackDummyEntity() {
        this.type = "dummy";
        this.health = 500;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return super.getBoundingBox();
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
