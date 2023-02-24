package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiState;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

import java.util.LinkedList;
import java.util.UUID;

public class PassiveAiStateMachine extends AiStateMachine {

    public LinkedList<UUID> avoidPlayers = new LinkedList<>();
    public Long lastAttack = 0L;

    public PassiveAiStateMachine() {
    }

    @Override
    public AiState onAttacked(HiveNetConnection by, LivingEntity attacked) {
        this.avoidPlayers.add(by.getUuid());
        this.lastAttack = System.currentTimeMillis();
        return AiState.RETREAT;
    }

    @Override
    public AiState onTick(LivingEntity entity) {

        // TODO: Add logic for random moving here :)
//        // Find a point to move to.
//        // Random point nearby
//        float a = RandomUtil.getRandomNumberBetween(0, 1);
//        float b = RandomUtil.getRandomNumberBetween(0, 1);
//        float th = (float) (b * 2 * Math.PI);
//        float rr = (float) (500 * Math.sqrt(a));
//        float x = (float) (rr * Math.cos(th));
//        float y = (float) (rr * Math.sin(th));
//
//        float h = DedicatedServer.instance.getWorld().generator.getHeightmap().getHeightFromLocation(new Location(x, y, 0));
//
//        Location target = new Location(x, y, h);
//        entity.addJob(new MoveToLocationJob(entity, target));

        return null;
    }
}