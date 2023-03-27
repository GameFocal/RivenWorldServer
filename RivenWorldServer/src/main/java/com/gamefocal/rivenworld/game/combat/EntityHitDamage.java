package com.gamefocal.rivenworld.game.combat;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;

public class EntityHitDamage extends HitDamage<HiveNetConnection, GameEntity> {

    public EntityHitDamage(HiveNetConnection a, GameEntity b, float damage) {
        super(a, b, damage);
    }

    public HiveNetConnection getPlayer() {
        return this.a;
    }

    public GameEntity getEntity() {
        return this.b;
    }
}
