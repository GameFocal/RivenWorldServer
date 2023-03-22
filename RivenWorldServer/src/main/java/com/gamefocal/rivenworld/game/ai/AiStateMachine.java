package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AiStateMachine {

    public HashMap<AiGoal, Integer> randomGoals = new HashMap<>();
    public AiGoal goal = null;
    public AiState state = AiState.PASSIVE;

    public AiStateMachine() {
    }

    public AiGoal randomWeightedGoal() {
        return RandomUtil.getRandomElementFromMap(this.randomGoals);
    }

    public void workGoal(LivingEntity livingEntity) {
        if (this.goal != null) {
            this.goal.onTick(livingEntity);
        }
    }

    public void assignGoal(AiGoal goal, LivingEntity livingEntity) {
        goal.onStart(livingEntity);
        this.goal = goal;
        livingEntity.isReadyForAI = true;
//        System.out.println("[AI]: Goal Assigned (" + goal.getClass().getSimpleName() + ")");
    }

    public void clearGoal(LivingEntity livingEntity) {
        if (this.goal != null) {
            this.goal.onEnd(livingEntity);
            this.goal = null;
//            System.out.println("[AI]: Goal Cleared");
        }
    }

    public void tick(LivingEntity entity) {
        this.workGoal(entity);
        this.onTick(entity);
    }

    public void netSync(Object data) {
        if (this.goal != null) {
//            this.goal.onNetSync(data);
        }
    }

    public abstract AiState onAttacked(HiveNetConnection by, LivingEntity attacked);

    public abstract AiState onSpooked(HiveNetConnection by, LivingEntity livingEntity, float influence);

    public abstract AiState onTick(LivingEntity entity);

    public void takeOwnership(LivingEntity livingEntity, HiveNetConnection connection) {
        if (this.goal != null) {
            this.goal.takeOwnership(livingEntity, connection);
        }
    }

    public void releaseOwnership(LivingEntity livingEntity) {
        if (this.goal != null) {
            this.goal.releaseOwnership(livingEntity);
        }
    }

    public boolean validatePeerUpdate(LivingEntity livingEntity, HiveNetConnection connection, Location location) {
        if (this.goal != null) {
            return this.goal.validatePeerUpdate(livingEntity, connection, location);
        }

        return false;
    }

    public void onOwnershipCmd(LivingEntity livingEntity, HiveNetConnection connection, String cmd, JsonObject data) {
        if (this.goal != null) {
            this.goal.onOwnershipCmd(livingEntity, connection, cmd, data);
        }
    }

}
