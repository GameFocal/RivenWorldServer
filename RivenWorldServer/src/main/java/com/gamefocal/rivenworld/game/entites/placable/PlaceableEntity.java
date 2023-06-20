package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public abstract class PlaceableEntity<T> extends DestructibleEntity<T> implements CollisionEntity {
    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
