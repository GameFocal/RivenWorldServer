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
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.items.generics.PlantableInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Poop;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.DirtyWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.SaltWaterBucket;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.concurrent.TimeUnit;

public class CropEntity<T> extends GameEntity<T> implements TickEntity, InteractableEntity {

    private static int totalStages = 4;
    private static long consumptionRateInMinutes = 120;

    private long lastConsumption = 0L;

    private CropType cropType = null;
    private int cropStage = 0;
    private boolean automatedCropStage = true;

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

        if (this.cropType != null) {
            if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.lastConsumption) > consumptionRateInMinutes && this.automatedCropStage) {
                this.cropStage = this.calculateGrowthStage();
                this.consumeWaterAndFood(0.01f);
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
        this.automatedCropStage = false;
        if (stage >= 0 && stage <= 4) {
            this.cropStage = stage;
        } else {
            throw new IllegalArgumentException("Invalid growth stage: " + stage);
        }
    }

    public void setAutomatedCropStage(boolean automatedCropStage) {
        this.automatedCropStage = automatedCropStage;
    }

    public void advanceGrowthStage() {
        this.automatedCropStage = false;
        if (this.cropStage < 4) {
            this.cropStage++;
        } else {
            throw new IllegalStateException("Cannot advance growth stage: crop is already dead");
        }
    }

    public double getCalcGrowthTime() {
        double growthTime = this.cropType.getTimeInMinutes();

        // Check if the crop has fertilizer
        if (this.fertilizer > 0) {
            growthTime *= (1 - 0.25 * this.fertilizer); // Fertilizer speeds up growth
        }

        // Check if the crop has water
        if (this.water > 0) {
            growthTime *= (1 - 0.25 * this.water); // Water speeds up growth by 25%
        }

        return growthTime;
    }

    public void consumeWaterAndFood(float val) {
        this.water -= val; // Decrease the water amount as it's used by the plant
        if (this.water < 0) this.water = 0; // Ensure water amount does not go below 0

        this.fertilizer -= val; // Decrease the fertilizer amount as it's used by the plant
        if (this.fertilizer < 0) this.fertilizer = 0; // Ensure fertilizer amount does not go below 0
    }

    public void resetGrowthStage() {
        this.cropStage = this.calculateGrowthStage();
    }

    public int calculateGrowthStage() {
        double growthTime = this.getCalcGrowthTime();

        // Calculate growth percentage
        double growthPercentage = (double) (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.plantedAt)) / growthTime;

        // Determine growth stage based on the growth percentage
        if (growthPercentage <= 0.20) {
            return 0; // Seed
        } else if (growthPercentage <= 0.40) {
            return 1; // Sprout
        } else if (growthPercentage <= 0.60) {
            return 2; // Grown
        } else if (growthPercentage <= 0.80) {
            return 3; // Fruit
        } else {
            return 4; // Dead
        }
    }

    public long timeInMinutesUntilGrown() {
        return (long) this.timeToReachStage(4);
    }

    public double timeToReachStage(int targetStage) {
        if (targetStage < 0 || targetStage > 4) {
            throw new IllegalArgumentException("Invalid target stage: " + targetStage);
        }
        double growthTime = this.getCalcGrowthTime();
        // Calculate the time to reach the target stage
        return growthTime * (targetStage * 0.20);
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {

        if (this.cropType != null && this.cropStage == 4) {
            // TODO: Harvest

            TaskService.schedulePlayerInterruptTask(() -> {
                InventoryStack[] yields = this.cropType.getYield();

                InventoryStack g = RandomUtil.getRandomElementFromArray(yields);

                if (g != null) {
                    g.setAmount(RandomUtil.getRandomNumberBetween(1, g.getAmount()));
                    connection.getPlayer().inventory.add(g);
                    connection.displayItemAdded(g);
                }

                this.clearCropType();

            }, 5L, "Harvesting", Color.GREEN, connection);
            return;
        }

        if (this.cropType != null) {
            TaskService.schedulePlayerInterruptTask(this::clearCropType, 5L, "Pulling Up Crop", Color.GREEN, connection);
            return;
        }

        if (inHand != null && Spade.class.isAssignableFrom(inHand.getItem().getClass())) {
            TaskService.schedulePlayerInterruptTask(this::clearCropType, 5L, "Digging Up Plot", Color.GREEN, connection);
            return;
        }

        if (inHand != null) {

            if (PlantableInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                // Is a seed
                if (inHand.getAmount() > 0) {
                    // has a seed

                    PlantableInventoryItem seedInventoryItem = (PlantableInventoryItem) inHand.getItem();

                    if (seedInventoryItem.crop() != null) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_DIRT, connection.getLookingAtTerrain(), 5000, 1, 1, 2);
                        TaskService.schedulePlayerInterruptTask(() -> {
                            /*
                             * Spawn the soil entity
                             * */
                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_ITEM, connection.getLookingAtTerrain(), 5000, 1, 1);
                            this.setPlantedCropType(seedInventoryItem.crop());
                            inHand.remove(1);
                            connection.updatePlayerInventory();
                        }, 2L, "Planting " + seedInventoryItem.crop().name(), Color.GREEN, connection);
                    }
                }
            } else if (DirtyWaterBucket.class.isAssignableFrom(inHand.getItem().getClass())) {
                if (inHand.getItem().canUseDurabilityOrBreak(25)) {
                    this.water += .25f;
                } else {
                    connection.breakItemInSlot(EquipmentSlot.PRIMARY);
                }
            } else if (CleanWaterBucket.class.isAssignableFrom(inHand.getItem().getClass())) {
                if (inHand.getItem().canUseDurabilityOrBreak(25)) {
                    this.water += .25f;
                } else {
                    connection.breakItemInSlot(EquipmentSlot.PRIMARY);
                }
            } else if (Poop.class.isAssignableFrom(inHand.getItem().getClass())) {
                this.fertilizer += .25f;
                inHand.remove(1);
            } else if (SaltWaterBucket.class.isAssignableFrom(inHand.getItem().getClass())) {
                if (inHand.getItem().canUseDurabilityOrBreak(25)) {
                    this.cropStage = 4;
                    this.automatedCropStage = false;
                } else {
                    connection.breakItemInSlot(EquipmentSlot.PRIMARY);
                }
            }

            connection.updatePlayerInventory();
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    public String cropStageToName() {
        switch (this.cropStage) {
            case 0:
                return "Seed";
            case 1:
                return "Sprout";
            case 2:
                return "Growing";
            case 3:
                return "Ready to Harvest";
            case 4:
                return "Dead";
        }

        return "Empty";
    }

    @Override
    public String helpText(HiveNetConnection connection) {
        if (this.cropType != null) {
            return this.cropType.name() + " / " + this.cropStageToName() + " / Water: " + (this.water * 100) + " % / Fertilizer: " + (this.fertilizer * 100) + "% / " + this.timeInMinutesUntilGrown() + " Minutes Left ";
        }

        return null;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {

        if (this.cropType != null && this.cropStage == 4) {
            // TODO: Harvest
            return "[e] Harvest";
        }

        if (this.cropType != null) {
            // TODO: Pull Up
            return "[e] Pull Up";
        }

        InventoryStack inHand = connection.getInHand();
        if (inHand != null) {
            if (PlantableInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                // Is a seed
                if (this.cropType == null) {
                    return "[e] Plant Seed";
                }
            } else if (Poop.class.isAssignableFrom(inHand.getItem().getClass())) {
                return "[e] Fertilize Plant";
            } else if (CleanWaterBucket.class.isAssignableFrom(inHand.getItem().getClass())) {
                return "[e] Water Plant";
            } else if (DirtyWaterBucket.class.isAssignableFrom(inHand.getItem().getClass())) {
                return "[e] Water Plant";
            } else if (Spade.class.isAssignableFrom(inHand.getItem().getClass())) {
                if (this.cropType == null) {
                    return "[e] Dig Up Plot";
                }
            }
        }

        return null;
    }
}
