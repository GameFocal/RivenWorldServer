package com.gamefocal.rivenworld.game.combat;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.World;

public class FallHitDamage extends HitDamage<World, HiveNetConnection> {
    public FallHitDamage(World a, HiveNetConnection b, float damage) {
        super(a, b, damage);
    }

    public HiveNetConnection getPlayer() {
        return this.b;
    }
}
