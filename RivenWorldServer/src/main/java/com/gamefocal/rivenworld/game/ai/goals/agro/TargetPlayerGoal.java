package com.gamefocal.rivenworld.game.ai.goals.agro;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.VectorUtil;

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
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        WorldGrid worldGrid = DedicatedServer.instance.getWorld().getGrid();

        Vector3 currentPosition = livingEntity.location.toVector();
        Vector3 targetPosition = this.target.getPlayer().location.toVector();

        // Calculate the new position by moving in the direction with the given speed
//        Vector3 newPosition = currentPosition.add(direction.scl(livingEntity.speed * deltaTime));

        Vector3 newPosition = currentPosition.cpy();
        Vector3 dir = targetPosition.cpy().sub(currentPosition).nor();
        newPosition.mulAdd(dir, livingEntity.speed);

        if (livingEntity.location.dist(this.target.getPlayer().location) > 200) {
            livingEntity.location = Location.fromVector(newPosition);
            double deg = VectorUtil.getDegrees(livingEntity.location.toVector(), targetPosition);
            livingEntity.location.setRotation(0, 0, (float) deg);
        } else {
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
