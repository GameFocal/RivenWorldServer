package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PeerVoteService;
import com.google.gson.JsonObject;

import java.util.Map;

public class LivingEntity<T> extends GameEntity<T> implements TickEntity {

    public float maxHealth = 100f;
    public float health = 100f;
    public float energy = 100f;
    public float food = 100f;
    public float water = 100f;
    public float speed = 85;
    public float awareness = 400;
    public boolean isResting = false;
    public boolean isFeeding = false;
    public transient AiStateMachine stateMachine;
    public transient HiveNetConnection owner;
    public transient boolean isReadyForAI = false;
    public transient float realVelocity = 0;

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

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
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

        // Sync speed and other data
        this.setMeta("resting", this.isResting);
        this.setMeta("speed", this.speed);
        this.setMeta("feeding", isFeeding);
        this.setMeta("vel", realVelocity);
    }

    @Override
    public void onTick() {
        if (this.stateMachine != null) {
            this.stateMachine.tick(this);
        }
    }
}
