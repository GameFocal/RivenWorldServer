package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

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

        if(this.aggro != null) {

            boolean closeGoal = false;

            // See if we should disengage
            float secondsSinceAggro = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.aggroStartAt);
            if(secondsSinceAggro >= this.aggroTimeLimitInSeconds) {
                closeGoal = true;
            }

            if(livingEntity.location.dist(this.aggroLocation) >= this.aggroLossDistance || livingEntity.location.dist(this.aggro.getPlayer().location) >= this.aggroLossDistance) {
                closeGoal = true;
            }

            if(closeGoal) {
                this.closeGoal(livingEntity);
                this.aggro = null;
                this.aggroStartAt = 0L;
                livingEntity.isAggro = false;
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
                livingEntity.specialState = "growl";
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BEAR_AGGRO, livingEntity.location, 2500, 1, 1);

                // New Target
                livingEntity.isAggro = true;
                this.aggro = close;
                this.aggroStartAt = System.currentTimeMillis();
                System.out.println("Aggro to " + close.getPlayer().displayName);
                this.assignGoal(livingEntity, new TargetPlayerGoal(close));
                this.aggroLocation = livingEntity.location.cpy();
            }
        }

        super.onTick(livingEntity);
    }
}
