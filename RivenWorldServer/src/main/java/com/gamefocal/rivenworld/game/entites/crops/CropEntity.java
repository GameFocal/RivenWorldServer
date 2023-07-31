package com.gamefocal.rivenworld.game.entites.crops;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.farming.CropStage;

import java.util.LinkedList;
import java.util.UUID;

public class CropEntity<T> extends GameEntity<T> implements TickEntity {

    private LinkedList<CropStage> stages = new LinkedList<>();
    private UUID attachedPlant = null;

    private float water = 0;
    private float fertilizer = 0;

    public CropEntity() {
        this.type = "TilledSoil";
    }

    public void addStage(long timeInSeconds, Class<? extends GameEntity> child) {
        this.stages.add(new CropStage(timeInSeconds, child));
    }

    public void addStage(CropStage stage) {
        this.stages.add(stage);
    }

    public UUID getAttachedPlant() {
        return attachedPlant;
    }

    public void setAttachedPlant(UUID attachedPlant) {
        this.attachedPlant = attachedPlant;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(float fertilizer) {
        this.fertilizer = fertilizer;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        // TODO: Crop tick here for growth
    }
}
