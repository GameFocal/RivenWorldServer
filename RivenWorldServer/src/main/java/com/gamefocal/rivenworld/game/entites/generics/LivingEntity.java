package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiState;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.google.gson.JsonObject;

public class LivingEntity<T> extends GameEntity<T> implements TickEntity {

    public float maxHealth = 100f;
    public float health = 100f;
    public float energy = 100f;
    public float food = 100f;
    public float water = 100f;
    public float speed = 85;
    public boolean isResting = false;
    public boolean isFeeding = false;
    public transient AiStateMachine stateMachine;

    public LivingEntity(float maxHealth, AiStateMachine stateMachine) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.stateMachine = stateMachine;
    }

    public LivingEntity() {
        this.maxHealth = 100f;
        this.health = maxHealth;
        this.stateMachine = new PassiveAiStateMachine();
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
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
    public void onSync() {
        super.onSync();
        if (this.stateMachine != null) {
            this.stateMachine.getState(this.meta);
        }

        // Sync speed and other data
        this.setMeta("resting", this.isResting);
        this.setMeta("speed", this.speed);
        this.setMeta("feeding", isFeeding);
    }

    @Override
    public void onTick() {
        if (this.stateMachine != null) {
            this.stateMachine.tick(this);
        }
    }
}
