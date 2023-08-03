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

import java.util.LinkedList;
import java.util.UUID;

public class CropEntity<T> extends GameEntity<T> implements TickEntity, InteractableEntity {
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
