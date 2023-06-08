package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.ai.goals.generic.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.PlayerUtil;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class AggroAiStateMachine extends PassiveAiStateMachine {

    public float aggroTriggerDistance = 500;
    public float aggroLossDistance = 500 * 5;
    public long aggroTimeLimitInSeconds = 60 * 3;
    public HiveNetConnection aggro = null;
    public long aggroStartAt = 0L;
    public Location aggroLocation = null;
    public HiveNetConnection seeking = null;
    public long lastSound = 0L;

    public AggroAiStateMachine(float aggroTriggerDistance, float aggroLossDistance, long aggroTimeLimitInSeconds) {
        this.aggroTriggerDistance = aggroTriggerDistance;
        this.aggroLossDistance = aggroLossDistance;
        this.aggroTimeLimitInSeconds = aggroTimeLimitInSeconds;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        /*
         * If aggro, chase the player
         * If not aggro, seek a player nearby that is not in light.
         * */

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

        LinkedList<HiveNetConnection> players = PlayerUtil.getClosestPlayers(livingEntity.location);
        if (players.size() > 0) {

            HiveNetConnection closestPlayer = players.get(0);

            // Cancel if closest is not the same
            if (currentGoal != null && (seeking == null || seeking.getUuid() != closestPlayer.getUuid())) {
                this.closeGoal(livingEntity);
                seeking = null;
            }

            // Seek the player
            if (this.currentGoal == null) {
                seeking = closestPlayer;
                this.assignGoal(livingEntity, new MoveToLocationGoal(closestPlayer.getPlayer().location, cell -> {
                    if (cell.getLightValue() > .5f) {
                        return false;
                    }

                    return true;
                }));
            }

            if (seeking != null) {
                livingEntity.speed = 2;
            }
        }

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastSound) >= 5) {
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.HELL_SOUND, livingEntity.location, 5000, 1, 1);
            lastSound = System.currentTimeMillis();
        }

        super.onTick(livingEntity);
    }

    //    @Override
//    public void onTick(LivingEntity livingEntity) {
//
//        if (this.aggro != null) {
//
//            boolean closeGoal = false;
//
//            // See if we should disengage
//            float secondsSinceAggro = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.aggroStartAt);
//            if (secondsSinceAggro >= this.aggroTimeLimitInSeconds) {
//                closeGoal = true;
//            }
//
//            if (livingEntity.location.dist(this.aggroLocation) >= this.aggroLossDistance || livingEntity.location.dist(this.aggro.getPlayer().location) >= this.aggroLossDistance) {
//                closeGoal = true;
//            }
//
//            if (closeGoal) {
//                this.closeGoal(livingEntity);
//                this.aggro = null;
//                this.aggroStartAt = 0L;
//                livingEntity.isAggro = false;
//            }
//        }
//
//        LinkedList<HiveNetConnection> inRange = PlayerUtil.getPlayersInRange(livingEntity.location, aggroTriggerDistance);
//
//        if (inRange.size() > 0) {
//            HiveNetConnection close = inRange.get(0);
//
//            if (this.aggro == null || !close.getPlayer().uuid.equalsIgnoreCase(this.aggro.getPlayer().uuid)) {
//                livingEntity.specialState = "growl";
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BEAR_AGGRO, livingEntity.location, 2500, 1, 1,5);
//
//                // New Target
//                livingEntity.isAggro = true;
//                this.aggro = close;
//                this.aggroStartAt = System.currentTimeMillis();
//                System.out.println("Aggro to " + close.getPlayer().displayName);
//                this.assignGoal(livingEntity, new TargetPlayerGoal(close));
//                this.aggroLocation = livingEntity.location.cpy();
//            }
//        }
//
//        super.onTick(livingEntity);
//    }
}
