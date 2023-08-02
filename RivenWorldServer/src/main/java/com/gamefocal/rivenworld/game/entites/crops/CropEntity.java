package com.gamefocal.rivenworld.game.entites.crops;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.farming.CropType;

import java.util.LinkedList;
import java.util.UUID;

public class CropEntity<T> extends GameEntity<T> implements TickEntity {
    private CropType cropType = null;
    private int cropStage = 0;

    private long plantedAt = 0L;
    private float water = 0;
    private float fertilizer = 0;

    public CropEntity() {
        this.type = "TilledSoil";
    }

    public void setPlantedCropType(CropType type) {
        this.cropType = type;
        this.plantedAt = System.currentTimeMillis();
        this.cropStage = 0;
    }

    public void clearCropType() {
        this.cropType = null;
        this.plantedAt = 0L;
    }

    public CropType getCropType() {
        return cropType;
    }

    public int getCropStage() {
        return cropStage;
    }

    public long getPlantedAt() {
        return plantedAt;
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
    public void onSync() {
        super.onSync();
        this.setMeta("stage", this.cropStage);
        this.setMeta("water", this.water);
        this.setMeta("ferta", this.fertilizer);
        this.setMeta("plant", (this.cropType != null) ? this.cropType.name() : null);
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
