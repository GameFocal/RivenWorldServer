package com.gamefocal.rivenworld.entites.combat.hits;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class CombatPlayerHitResult extends CombatHitResult {

    private HiveNetConnection from;
    private HiveNetConnection hit;

    public CombatPlayerHitResult(HiveNetConnection from, HiveNetConnection hit, Vector3 hitAt) {
        this.from = from;
        this.hit = hit;
        this.hitAt = hitAt;
    }

    public HiveNetConnection getFrom() {
        return from;
    }

    public void setFrom(HiveNetConnection from) {
        this.from = from;
    }

    public HiveNetConnection getHit() {
        return hit;
    }

    public void setHit(HiveNetConnection hit) {
        this.hit = hit;
    }

    public Vector3 getHitAt() {
        return hitAt;
    }

    public void setHitAt(Vector3 hitAt) {
        this.hitAt = hitAt;
    }

    @Override
    public void onHit(float amt) {
        this.hit.takeDamage(amt);
    }
}
