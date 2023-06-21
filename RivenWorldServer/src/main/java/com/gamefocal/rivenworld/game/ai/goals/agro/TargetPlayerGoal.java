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
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.google.common.base.Objects;

import java.util.concurrent.TimeUnit;

public class TargetPlayerGoal extends AiGoal {

    protected HiveNetConnection target;
    protected long lastAttack = 0L;

    public TargetPlayerGoal(HiveNetConnection target) {
        this.target = target;
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
        TargetPlayerGoal that = (TargetPlayerGoal) o;
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

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - livingEntity.lastAttacked) <= 5) {
            livingEntity.speed = .75f;
        } else {
            livingEntity.speed = 3;
        }

        this.target.markInCombat();

        Vector3 currentPosition = livingEntity.location.toVector();
        Vector3 targetPosition = this.target.getPlayer().location.toVector();

        Vector3 dir = targetPosition.cpy().sub(currentPosition).nor();
        livingEntity.setVelocity(dir);
        livingEntity.setLocationGoal(this.target.getPlayer().location.toVector());

        livingEntity.isMoving = true;
        if (livingEntity.location.dist(this.target.getPlayer().location) > 200) {
            livingEntity.setVelocity(dir);
        } else {
            livingEntity.resetVelocity();
            livingEntity.isMoving = false;
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastAttack) > 2) {
                // Attack
                livingEntity.specialState = "bite";
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BEAR_AGGRO, livingEntity.location, 1500, 1, 1, 5);

                this.lastAttack = System.currentTimeMillis();

                // TODO: Deal Damage to target
                livingEntity.attackPlayer(this.target);

                System.err.println("ATTACK");
            }
        }

        livingEntity.speed = 3;

        // use livingEntities smarttraversal
    }
}
