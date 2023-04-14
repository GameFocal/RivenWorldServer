package com.gamefocal.rivenworld.game.entites.generics;

import com.badlogic.gdx.math.collision.BoundingBox;

public interface CollisionEntity {

    BoundingBox getBoundingBox();

    void takeDamage(float amt);

}
