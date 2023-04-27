package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AiStateMachine {

    protected LivingEntity entity = null;
    protected AiGoal currentGoal = null;
    protected ConcurrentLinkedQueue<AiGoal> queue = new ConcurrentLinkedQueue<>();

    public void tick(LivingEntity livingEntity) {

        this.onTick(livingEntity);

        if (this.entity == null) {
            this.entity = livingEntity;
        }

        if (currentGoal == null && this.queue.size() > 0) {
            // Has a goal... assign the goal to the AI
            currentGoal = this.queue.poll();
            currentGoal.attach(livingEntity);
        }

        if (currentGoal != null) {
//            System.out.println("GOAL TICK");
            currentGoal.onTick(livingEntity);

            if(currentGoal.isComplete) {
                System.out.println("IS COMPETE");
                currentGoal = null;
            }
        }
    }

    public void queueGoal(LivingEntity livingEntity, AiGoal goal) {
        this.queue.add(goal);
    }

    public void assignGoal(LivingEntity livingEntity, AiGoal goal) {
        if (this.currentGoal != null) {
            this.currentGoal.onComplete(this.entity);
        }
        this.currentGoal = goal;
        this.currentGoal.attach(livingEntity);
    }

    public abstract void onTick(LivingEntity livingEntity);
}
