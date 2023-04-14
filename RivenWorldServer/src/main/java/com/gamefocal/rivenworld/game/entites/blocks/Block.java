package com.gamefocal.rivenworld.game.entites.blocks;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public abstract class Block<A> extends DestructibleEntity<A> implements CollisionEntity {

    public Block() {
        this.useSpacialLoading = false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
