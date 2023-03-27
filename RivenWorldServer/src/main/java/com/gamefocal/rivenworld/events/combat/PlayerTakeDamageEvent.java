package com.gamefocal.rivenworld.events.combat;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.combat.HitDamage;
import com.gamefocal.rivenworld.game.ray.HitResult;

public class PlayerTakeDamageEvent extends Event<PlayerTakeDamageEvent> {

    private HiveNetConnection player;

    private float damage;

    private HitResult hitResult;

    private HitDamage hitDamage;

    public PlayerTakeDamageEvent(HiveNetConnection player, float damage, HitResult hitResult, HitDamage hitDamage) {
        this.player = player;
        this.damage = damage;
        this.hitResult = hitResult;
        this.hitDamage = hitDamage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
        this.hitDamage.setDamage(damage);
    }

    public HiveNetConnection getPlayer() {
        return player;
    }

    public float getDamage() {
        return damage;
    }

    public HitResult getHitResult() {
        return hitResult;
    }

    public HitDamage getHitDamage() {
        return hitDamage;
    }
}
