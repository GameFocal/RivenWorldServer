package com.gamefocal.rivenworld.entites.combat;

import com.badlogic.gdx.math.Vector3;

public abstract class CombatHitResult {

    protected Vector3 hitAt;

    public abstract void onHit(float amt);

    public Vector3 getHitAt() {
        return hitAt;
    }
}
