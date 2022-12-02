package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.inventory.Inventory;
import com.google.auto.service.AutoService;
import com.j256.ormlite.stmt.query.In;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class InventoryService implements HiveService<InventoryService> {

    private Hashtable<UUID, Inventory> inventories = new Hashtable<>();

    @Override
    public void init() {

    }

    public void trackInventory(Inventory inventory) {
        this.inventories.put(inventory.getUuid(), inventory);
    }

    public void untrackInventory(Inventory inventory) {
        this.inventories.remove(inventory.getUuid());
    }

    public void untrackInventory(UUID uuid) {
        this.inventories.remove(uuid);
    }

    public Inventory getInvFromId(UUID uuid) {
        if (this.inventories.containsKey(uuid)) {
            return this.inventories.get(uuid);
        }

        return null;
    }

    public Hashtable<UUID, Inventory> getInventories() {
        return inventories;
    }
}
