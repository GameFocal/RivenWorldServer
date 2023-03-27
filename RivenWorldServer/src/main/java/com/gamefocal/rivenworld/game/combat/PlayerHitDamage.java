package com.gamefocal.rivenworld.game.combat;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerHitDamage extends HitDamage<HiveNetConnection, HiveNetConnection> {
    public PlayerHitDamage(HiveNetConnection a, HiveNetConnection b, float damage) {
        super(a, b, damage);
    }

    public HiveNetConnection getFrom() {
        return this.a;
    }

    public HiveNetConnection getAttacked() {
        return this.b;
    }
}
