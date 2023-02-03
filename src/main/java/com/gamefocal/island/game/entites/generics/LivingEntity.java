package com.gamefocal.island.game.entites.generics;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.ai.AiState;
import com.gamefocal.island.game.ai.AiStateMachine;

public class LivingEntity<T> extends GameEntity<T> {

    public float maxHealth = 100f;
    public float health = 100f;
    public float energy = 100f;
    public float food = 100f;
    public float water = 100f;
    public AiState state;
    public AiStateMachine stateMachine;

    public LivingEntity(float maxHealth, AiStateMachine stateMachine) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.stateMachine = stateMachine;
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

    }
}
