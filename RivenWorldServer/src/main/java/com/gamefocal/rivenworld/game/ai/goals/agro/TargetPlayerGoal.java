package com.gamefocal.rivenworld.game.ai.goals.agro;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.goals.generic.FastMoveToLocation;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.game.world.EnclosureScanner;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class TargetPlayerGoal extends FastMoveToLocation {

    protected HiveNetConnection target;
    protected long lastAttack = 0L;
    protected Location targetTrackLocation = null;

    public TargetPlayerGoal(HiveNetConnection target) {
        super(target.getPlayer().location);
        this.target = target;
//        this.goal = target.getPlayer().location.cpy();
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        super.onStart(livingEntity);
        this.target.markInCombat();
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {
        super.complete(livingEntity);
        this.target = null;
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
    public void updateActualLocation(LivingEntity livingEntity) {
        this.actualLocation = this.target.getPlayer().location;
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

        super.onTick(livingEntity);

        this.target.markInCombat();

        livingEntity.isMoving = true;
        if (livingEntity.location.dist(this.target.getPlayer().location) <= 200) {
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
    }
}
