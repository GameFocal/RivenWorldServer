package com.gamefocal.island.game.entites.generics;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.ai.AiJob;
import com.gamefocal.island.game.ai.AiState;
import com.gamefocal.island.game.ai.AiStateMachine;
import com.gamefocal.island.game.ai.machines.PassiveAiStateMachine;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LivingEntity<T> extends GameEntity<T> {

    public float maxHealth = 100f;
    public float health = 100f;
    public float energy = 100f;
    public float food = 100f;
    public float water = 100f;
    public float speed = 5;
    public transient AiState state;
    public transient AiStateMachine stateMachine;
    public transient ConcurrentLinkedQueue<AiJob> jobs = new ConcurrentLinkedQueue<>();
    public transient AiJob currentJob = null;

    public LivingEntity(float maxHealth, AiStateMachine stateMachine) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.stateMachine = stateMachine;
        this.state = AiState.PASSIVE;
    }

    public LivingEntity() {
        this.maxHealth = 100f;
        this.health = maxHealth;
        this.stateMachine = new PassiveAiStateMachine();
        this.state = AiState.PASSIVE;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void addJob(AiJob job) {
        this.jobs.add(job);
    }

    public AiJob getCurrentJob() {
        return currentJob;
    }

    public AiJob peekNextJob() {
        return this.jobs.peek();
    }

    public AiJob nextJob() {
        return this.jobs.poll();
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public void setState(AiState state) {
        this.state = state;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public float getEnergy() {
        return energy;
    }

    public float getFood() {
        return food;
    }

    public float getWater() {
        return water;
    }

    public AiState getState() {
        return state;
    }

    public AiStateMachine getStateMachine() {
        return stateMachine;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        /*
        * Job System
        * */
        if (this.currentJob == null) {
            if (this.jobs.size() > 0) {
                this.currentJob = this.nextJob();
                this.currentJob.onStart(this);
            }
        } else if(this.currentJob.isComplete()) {
            // Is Complete
            this.currentJob.onComplete(this);
            this.currentJob = null;
        } else {
            this.currentJob.onWork(this);
        }
    }
}
