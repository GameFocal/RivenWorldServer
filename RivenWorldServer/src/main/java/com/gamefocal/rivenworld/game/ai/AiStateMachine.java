package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.game.ai.goals.agro.AvoidPlayerGoal;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.weather.GameWeather;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public abstract class AiStateMachine {

    protected boolean isReady = false;
    protected LivingEntity entity = null;
    protected AiGoal currentGoal = null;
    protected ConcurrentLinkedQueue<AiGoal> queue = new ConcurrentLinkedQueue<>();

    protected Class<? extends AiGoal> lastGoal = null;

    protected HashMap<Class<? extends AiGoal>, HashMap<AiGoal, Integer>> goalTable = new HashMap<>();

    public void tick(LivingEntity livingEntity) {

        if (!this.isReady) {
            this.onInit(livingEntity);
            this.isReady = true;
        }

        this.onTick(livingEntity);

        /*
         * Check if the living entity is attacked and apply logic based on it's mode
         * */
        boolean hasBeenAttacked = (livingEntity.lastAttackedBy != null && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - livingEntity.lastAttacked) <= 30);

        if (hasBeenAttacked && livingEntity.attackResponse) {
            livingEntity.attackResponse = false;

            AiGoal goalToAssign = null;
            if (livingEntity.aiBehavior == AiBehavior.PASSIVE) {
                // Run Away
                goalToAssign = new AvoidPlayerGoal(livingEntity.lastAttackedBy);
            } else if (livingEntity.aiBehavior == AiBehavior.PASSIVE_AGGRESSIVE) {
                // Attack then run away at certain health
                if (livingEntity.health >= (livingEntity.maxHealth * .25)) {
                    goalToAssign = new TargetPlayerGoal(livingEntity.lastAttackedBy);
                }
            } else if (livingEntity.aiBehavior == AiBehavior.AGGRESSIVE) {
                // Attack to no end
                goalToAssign = new TargetPlayerGoal(livingEntity.lastAttackedBy);
            }

            if (this.currentGoal != null && !this.currentGoal.equals(goalToAssign)) {
                this.currentGoal.complete(livingEntity);
            }
            this.assignGoal(livingEntity, goalToAssign);
        }

        if (this.currentGoal != null && livingEntity.aiBehavior == AiBehavior.PASSIVE_AGGRESSIVE && TargetPlayerGoal.class.isAssignableFrom(this.currentGoal.getClass())) {
            if (livingEntity.health < (livingEntity.maxHealth * .25)) {
                this.closeGoal(livingEntity);
                this.assignGoal(livingEntity, new AvoidPlayerGoal(livingEntity.lastAttackedBy));
            }
        }

        if (this.entity == null) {
            this.entity = livingEntity;
        }

        if (currentGoal == null && this.queue.size() > 0) {
            // Has a goal... assign the goal to the AI
            currentGoal = this.queue.poll();
            currentGoal.attach(livingEntity);
            lastGoal = currentGoal.getClass();
        }

        if (currentGoal != null) {
//            System.out.println("GOAL TICK");
            currentGoal.onTick(livingEntity);

            if (currentGoal.isComplete) {
                AiGoal next = this.currentGoal.getNext();
                this.closeGoal(livingEntity);
                if (next != null) {
                    this.assignGoal(livingEntity, next);
                }
            }
        }
    }

    public void closeGoal(LivingEntity livingEntity) {
        if (this.currentGoal != null) {
            this.currentGoal.onComplete(livingEntity);
            this.currentGoal = null;
        }
    }

    public void queueGoal(LivingEntity livingEntity, AiGoal goal) {
        this.queue.add(goal);
    }

    public void assignGoal(LivingEntity livingEntity, AiGoal goal) {
        if (this.currentGoal != null) {
            this.closeGoal(livingEntity);
        }
        this.currentGoal = goal;
        this.currentGoal.attach(livingEntity);
        lastGoal = currentGoal.getClass();
    }

    public abstract void onTick(LivingEntity livingEntity);

    public abstract void onInit(LivingEntity livingEntity);

    public AiGoal newRandomGoal() {
        if (this.goalTable.containsKey(this.lastGoal)) {
            return RandomUtil.getRandomElementFromMap(this.goalTable.get(this.lastGoal));
        }

        return null;
    }
}
