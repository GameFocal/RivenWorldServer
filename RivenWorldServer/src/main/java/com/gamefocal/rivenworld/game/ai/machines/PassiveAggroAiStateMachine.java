package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class PassiveAggroAiStateMachine extends PassiveAiStateMachine {

    public float aggroTriggerDistance = 500;
    public float aggroLossDistance = 500 * 5;
    public long aggroTimeLimitInSeconds = 60 * 3;
    public HiveNetConnection aggro = null;
    public long aggroStartAt = 0L;

    public PassiveAggroAiStateMachine(float aggroTriggerDistance, float aggroLossDistance, long aggroTimeLimitInSeconds) {
        this.aggroTriggerDistance = aggroTriggerDistance;
        this.aggroLossDistance = aggroLossDistance;
        this.aggroTimeLimitInSeconds = aggroTimeLimitInSeconds;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        /*
         * Aggro Logic
         * */
        if (aggro != null) {
            if (livingEntity.location.dist(this.aggro.getPlayer().location) > this.aggroLossDistance || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.aggroStartAt) > this.aggroTimeLimitInSeconds) {
                if (this.currentGoal != null) {
                    this.currentGoal.complete(livingEntity);
                }
                this.aggro = null;
                this.aggroStartAt = 0;
            }
        }

        LinkedList<HiveNetConnection> inRange = new LinkedList<>();
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (connection.getPlayer().location.dist(livingEntity.location) <= this.aggroTriggerDistance) {
                inRange.add(connection);
            }
        }

        if (inRange.size() > 0) {
            inRange.sort((o1, o2) -> {
                float dst1 = o1.getPlayer().location.dist(livingEntity.location);
                float dst2 = o2.getPlayer().location.dist(livingEntity.location);

                if (dst2 < dst1) {
                    return +1;
                } else if (dst2 > dst1) {
                    return -1;
                }

                return 0;
            });

            // Close
            HiveNetConnection close = inRange.getFirst();

            if (this.aggro == null || !close.getPlayer().uuid.equalsIgnoreCase(this.aggro.getPlayer().uuid)) {
                // New Target
                this.aggro = close;
                this.aggroStartAt = System.currentTimeMillis();
                System.out.println("Aggro to " + close.getPlayer().displayName);
                this.assignGoal(livingEntity, new TargetPlayerGoal(close));
            }
        }

        super.onTick(livingEntity);
    }
}
