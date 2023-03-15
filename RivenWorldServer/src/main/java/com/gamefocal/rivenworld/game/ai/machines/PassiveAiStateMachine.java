package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiState;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.FeedGoal;
import com.gamefocal.rivenworld.game.ai.goals.RandomLocationGoal;
import com.gamefocal.rivenworld.game.ai.goals.RestGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

import java.util.LinkedList;
import java.util.UUID;

public class PassiveAiStateMachine extends AiStateMachine {

    public LinkedList<UUID> avoidPlayers = new LinkedList<>();
    public Long lastAttack = 0L;

    public PassiveAiStateMachine() {
        this.randomGoals.put(new RandomLocationGoal(), 20);
//        this.randomGoals.put(new FeedGoal(), 10);
//        this.randomGoals.put(new RestGoal(), 5);
    }

    @Override
    public AiState onAttacked(HiveNetConnection by, LivingEntity attacked) {
        this.avoidPlayers.add(by.getUuid());
        this.lastAttack = System.currentTimeMillis();
        return AiState.RETREAT;
    }

    @Override
    public AiState onTick(LivingEntity entity) {

        // If no Goal assign one
        if (this.goal == null) {
            // No goal so we start with one.
            if (this.state == AiState.PASSIVE) {
                this.assignGoal(this.randomWeightedGoal(), entity);
            }
        }

        if (this.goal != null && this.goal.isComplete()) {
            this.goal = null;
        }

        return null;
    }
}
