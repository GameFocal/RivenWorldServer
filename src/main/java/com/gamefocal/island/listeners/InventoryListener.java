package com.gamefocal.island.listeners;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.events.inv.InventoryUpdateEvent;
import com.gamefocal.island.game.entites.storage.DropBag;
import com.gamefocal.island.game.entites.storage.StorageEntity;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.service.InventoryService;

public class InventoryListener implements EventInterface {

    @EventHandler
    public void onInventoryUpdated(InventoryUpdateEvent event) {
        if (event.getInventory().getAttachedEntity() != null && event.getInventory().isEmpty()) {
            if (event.getInventory().getAttachedEntity() != null) {
                if (StorageEntity.class.isAssignableFrom(event.getInventory().getAttachedEntity().getClass())) {
                    ((StorageEntity) event.getInventory().getAttachedEntity()).onInventoryUpdated();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getAttachedEntity() != null && event.getInventory().isEmpty()) {
            if (event.getInventory().getAttachedEntity() != null) {
                if (StorageEntity.class.isAssignableFrom(event.getInventory().getAttachedEntity().getClass())) {
                    ((StorageEntity) event.getInventory().getAttachedEntity()).onInventoryOpen();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryCloseEvent event) {
        if (event.getInventory().getAttachedEntity() != null && event.getInventory().isEmpty()) {
            if (event.getInventory().getAttachedEntity() != null) {
                if (StorageEntity.class.isAssignableFrom(event.getInventory().getAttachedEntity().getClass())) {
                    ((StorageEntity) event.getInventory().getAttachedEntity()).onInventoryClosed();
                }
            }
        }
    }

}
