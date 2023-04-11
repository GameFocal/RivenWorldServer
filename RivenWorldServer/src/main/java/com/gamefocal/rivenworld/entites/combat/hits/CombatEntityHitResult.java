package com.gamefocal.rivenworld.entites.combat.hits;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;

public class CombatEntityHitResult extends CombatHitResult {

    private HiveNetConnection from;
    private GameEntity hit;
    private Vector3 hitAt;

    public CombatEntityHitResult(HiveNetConnection from, GameEntity hit, Vector3 hitAt) {
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

    public GameEntity getHit() {
        return hit;
    }

    public void setHit(GameEntity hit) {
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
        if (CollisionEntity.class.isAssignableFrom(this.hit.getClass())) {
            CollisionEntity collisionEntity = (CollisionEntity) this.hit;
            collisionEntity.takeDamage(amt);
        }
    }
}
