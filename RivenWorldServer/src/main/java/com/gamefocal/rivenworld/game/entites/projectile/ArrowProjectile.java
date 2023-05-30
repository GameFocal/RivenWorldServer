package com.gamefocal.rivenworld.game.entites.projectile;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class ArrowProjectile extends FlyingProjectile<ArrowProjectile> {

    public ArrowProjectile(HiveNetConnection firedBy, float speed) {
        super(firedBy, speed);
        this.type = "NetArrow";
        this.damage = 10;
        this.despawnOnHit = true;
    }
}
