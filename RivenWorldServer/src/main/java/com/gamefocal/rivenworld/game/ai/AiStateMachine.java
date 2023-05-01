package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.weather.GameWeather;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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
