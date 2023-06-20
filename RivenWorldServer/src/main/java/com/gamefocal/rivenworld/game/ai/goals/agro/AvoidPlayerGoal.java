package com.gamefocal.rivenworld.game.ai.goals.agro;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.google.common.base.Objects;

import java.util.concurrent.TimeUnit;

public class AvoidPlayerGoal extends AiGoal {

    protected HiveNetConnection target;
    protected long lastAttack = 0L;

    public AvoidPlayerGoal(HiveNetConnection target) {
        this.target = target;
    }

    public HiveNetConnection getTarget() {
        return target;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        livingEntity.isMoving = true;
        this.target.markInCombat();
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvoidPlayerGoal that = (AvoidPlayerGoal) o;
        return Objects.equal(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        if (this.target.getPlayer().playerStats.health <= 0) {
            // Is dead
            this.complete(livingEntity);
            return;
        }

        if (this.target.getPlayer().location.dist(livingEntity.location) >= 7500) {
            this.complete(livingEntity);
            return;
        }

        this.target.markInCombat();

        Vector3 awayFromAvoid = new Vector3(livingEntity.location.toVector());
        awayFromAvoid.sub(this.target.getPlayer().location.toVector());
        awayFromAvoid.nor();

        // Set Velocity
        livingEntity.setVelocity(awayFromAvoid);
        livingEntity.setLocationGoal(livingEntity.location.toVector().mulAdd(awayFromAvoid, 2000));

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - livingEntity.lastAttacked) <= 5) {
            livingEntity.speed = .75f;
        } else {
            livingEntity.speed = 3;
        }
    }
}
