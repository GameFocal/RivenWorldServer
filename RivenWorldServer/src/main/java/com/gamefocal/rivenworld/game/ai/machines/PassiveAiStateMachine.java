package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.AiState;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.FeedGoal;
import com.gamefocal.rivenworld.game.ai.goals.RandomLocationGoal;
import com.gamefocal.rivenworld.game.ai.goals.RestGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.RandomUtil;

import java.util.LinkedList;
import java.util.UUID;

public class PassiveAiStateMachine extends AiStateMachine {

    public LinkedList<UUID> avoidPlayers = new LinkedList<>();
    public Long lastAttack = 0L;
    private Class<? extends AiGoal> lastGoalType = null;

    public PassiveAiStateMachine() {
//        this.randomGoals.put(new RandomLocationGoal(), 10);
//        this.randomGoals.put(new FeedGoal(), 2);
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
                if (RandomUtil.getRandomChance(.15) && (this.lastGoalType == null || !FeedGoal.class.isAssignableFrom(this.lastGoalType))) {
                    this.assignGoal(new FeedGoal(), entity);
                    this.lastGoalType = FeedGoal.class;
                } else {
                    this.assignGoal(new RandomLocationGoal(), entity);
                    this.lastGoalType = RandomLocationGoal.class;
                }
            }
        }

        if (this.goal != null && this.goal.isComplete()) {
            this.goal.onEnd(entity);
            this.goal = null;
        }

        return null;
    }
}
