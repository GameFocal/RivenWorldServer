package com.gamefocal.island.game.entites.storage;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class StorageEntity<T> extends GameEntity<T> {

    protected Inventory inventory;

    @Override
    public void onSync() {
        if (this.inventory != null) {
            this.setMeta("invid", this.inventory.getUuid().toString());
            this.setMeta("inv", Base64.getEncoder().encodeToString(InventoryUtil.inventoryToJson(this.inventory).toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void onSpawn() {
        // Add inventory to tracking
        DedicatedServer.get(InventoryService.class).trackInventory(this.inventory);
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    public abstract void onInventoryUpdated();

    public abstract void onInventoryOpen();

    public abstract void onInventoryClosed();
}
