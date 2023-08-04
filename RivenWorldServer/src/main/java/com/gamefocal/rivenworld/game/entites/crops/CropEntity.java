package com.gamefocal.rivenworld.game.entites.crops;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.concurrent.TimeUnit;

public class CropEntity<T> extends GameEntity<T> implements TickEntity, InteractableEntity {

    private static int totalStages = 4;
    private static long consumptionRateInMinutes = 120;

    private long lastConsumption = 0L;

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

        if(this.cropType != null) {
            if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.lastConsumption) > consumptionRateInMinutes) {
                this.cropStage = this.calculateGrowthStage(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.plantedAt));
                this.lastConsumption = System.currentTimeMillis();
            }
        }
    }

    public void resetWater() {
        this.water = 0;
    }

    public void resetFertilizer() {
        this.fertilizer = 0;
    }

    public void setGrowthStage(int stage) {
        if(stage >= 0 && stage <= 4) {
            this.cropStage = stage;
        } else {
            throw new IllegalArgumentException("Invalid growth stage: " + stage);
        }
    }

    public void advanceGrowthStage() {
        if(this.cropStage < 4) {
            this.cropStage++;
        } else {
            throw new IllegalStateException("Cannot advance growth stage: crop is already dead");
        }
    }

    public int calculateGrowthStage(long minutesPassed) {
        double growthTime = this.cropType.getTimeInMinutes();

        // Check if the crop has fertilizer
        if(this.fertilizer > 0) {
            growthTime *= (1 - 0.25 * this.fertilizer); // Fertilizer speeds up growth
            this.fertilizer -= 0.01; // Decrease the fertilizer amount as it's used by the plant
            if(this.fertilizer < 0) this.fertilizer = 0; // Ensure fertilizer amount does not go below 0
        }

        // Check if the crop has water
        if(this.water > 0) {
            growthTime *= (1 - 0.25 * this.water); // Water speeds up growth by 25%
            this.water -= 0.01; // Decrease the water amount as it's used by the plant
            if(this.water < 0) this.water = 0; // Ensure water amount does not go below 0
        }

        // Calculate growth percentage
        double growthPercentage = (double) minutesPassed / growthTime;

        // Determine growth stage based on the growth percentage
        if(growthPercentage <= 0.20) {
            return  0; // Seed
        } else if(growthPercentage <= 0.40) {
            return 1; // Sprout
        } else if(growthPercentage <= 0.60) {
            return 2; // Grown
        } else if(growthPercentage <= 0.80) {
            return 3; // Fruit
        } else {
            return 4; // Dead
        }
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (inHand != null) {
            if (SeedInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                // Is a seed
                if (inHand.getAmount() > 0) {
                    // has a seed

                    SeedInventoryItem seedInventoryItem = (SeedInventoryItem) inHand.getItem();

                    if (seedInventoryItem.getPlantType() != null) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_DIRT, connection.getLookingAtTerrain(), 5000, 1, 1, 2);
                        TaskService.schedulePlayerInterruptTask(() -> {
                            /*
                             * Spawn the soil entity
                             * */
                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_ITEM, connection.getLookingAtTerrain(), 5000, 1, 1);
                            this.setPlantedCropType(seedInventoryItem.getPlantType());
                            inHand.remove(1);
                            connection.updatePlayerInventory();
                        }, 2L, "Planting " + seedInventoryItem.getPlantType().name(), Color.BLUE, connection);
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (this.cropType != null) {
            return this.cropType.name() + "(0%)";
        }

        return null;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        InventoryStack inHand = connection.getInHand();
        if (inHand != null) {
            if (SeedInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                // Is a seed
                if (this.cropType == null) {
                    return "[e] Plant Seed";
                }
            }
        }

        return null;
    }
}
