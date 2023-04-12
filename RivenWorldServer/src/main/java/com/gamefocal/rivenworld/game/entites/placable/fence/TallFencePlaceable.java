package com.gamefocal.rivenworld.game.entites.placable.fence;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class TallFencePlaceable extends PlaceableEntity<TallFencePlaceable> implements CollisionEntity {

    public TallFencePlaceable() {
        this.type = "TallFencePlaceable";
        this.configureAlwaysLoaded();
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
    public BoundingBox collisionBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(),50,100);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
