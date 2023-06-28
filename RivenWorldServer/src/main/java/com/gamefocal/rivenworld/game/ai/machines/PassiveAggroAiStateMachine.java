package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.PlayerUtil;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class PassiveAggroAiStateMachine extends PassiveAiStateMachine {

    public float aggroTriggerDistance = 500;
    public float aggroLossDistance = 500 * 5;
    public long aggroTimeLimitInSeconds = 60 * 3;
    public HiveNetConnection aggro = null;
    public long aggroStartAt = 0L;
    public Location aggroLocation = null;

    public PassiveAggroAiStateMachine(float aggroTriggerDistance, float aggroLossDistance, long aggroTimeLimitInSeconds) {
        this.aggroTriggerDistance = aggroTriggerDistance;
        this.aggroLossDistance = aggroLossDistance;
        this.aggroTimeLimitInSeconds = aggroTimeLimitInSeconds;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        if (this.aggro != null) {

            boolean closeGoal = false;

            // See if we should disengage
            float secondsSinceAggro = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.aggroStartAt);
            if (secondsSinceAggro >= this.aggroTimeLimitInSeconds) {
                closeGoal = true;
            }

            if (livingEntity.location.dist(this.aggroLocation) >= this.aggroLossDistance || livingEntity.location.dist(this.aggro.getPlayer().location) >= this.aggroLossDistance) {
                closeGoal = true;
            }

            if (closeGoal) {
                this.closeGoal(livingEntity);
                this.aggro = null;
                this.aggroStartAt = 0L;
                livingEntity.isAggro = false;
            }
        }

        if (this.aggro == null) {
            LinkedList<HiveNetConnection> inRange = PlayerUtil.getPlayersInRange(livingEntity.location, aggroTriggerDistance);

            while (inRange.size() > 0) {
                HiveNetConnection close = inRange.poll();

                WorldCell startCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(livingEntity.location);
                WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(close.getPlayer().location);

                if (!startCell.hasGridLineOfSight(goalCell)) {
                    continue;
                }

                if (!livingEntity.canAggroToPlayer(close)) {
                    continue;
                }

                livingEntity.specialState = "growl";
                DedicatedServer.instance.getWorld().playSoundAtLocation(livingEntity.aggroSound, livingEntity.location, 2500, 1, 1, 5);

                // New Target
                livingEntity.isAggro = true;
                this.aggro = close;
                this.aggroStartAt = System.currentTimeMillis();
//                System.out.println("Aggro to " + close.getPlayer().displayName);

                TargetPlayerGoal targetPlayerGoal = new TargetPlayerGoal(close);
                targetPlayerGoal.setMaxDistance(20);

                this.assignGoal(livingEntity, targetPlayerGoal);
                this.aggroLocation = livingEntity.location.cpy();
                break;
            }
        } else if (!livingEntity.isAggro) {
            this.aggro = null;
            this.aggroStartAt = 0L;
        }

        super.onTick(livingEntity);
    }
}
