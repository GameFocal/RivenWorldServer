package com.gamefocal.rivenworld.game.entites.generics;

import com.badlogic.gdx.math.collision.BoundingBox;

public interface CollisionEntity {

    BoundingBox collisionBox();

    void takeDamage(float amt);

}
