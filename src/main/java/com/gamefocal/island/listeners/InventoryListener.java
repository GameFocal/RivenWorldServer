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
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

public class InventoryListener implements EventInterface {

    @EventHandler
    public void onInventoryUpdated(InventoryUpdateEvent event) {
        UUID attachedUUID = event.getInventory().getAttachedEntity();
        if (attachedUUID != null) {
            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(attachedUUID);
            if (e != null) {
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {
                    // Is a storage entity
                    StorageEntity se = (StorageEntity) e.entityData;
                    se.onInventoryUpdated();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        UUID attachedUUID = event.getInventory().getAttachedEntity();
        if (attachedUUID != null) {
            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(attachedUUID);
            if (e != null) {
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {
                    // Is a storage entity
                    StorageEntity se = (StorageEntity) e.entityData;
                    se.onInventoryOpen();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event) {

        System.out.println("CALL");

        UUID attachedUUID = event.getInventory().getAttachedEntity();
        if (attachedUUID != null) {

            System.out.println("ATTACHED TO: " + attachedUUID);

            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(attachedUUID);
            if (e != null) {

                System.out.println("Found Entity");

                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {

                    System.out.println("Is Storage Entity");

                    // Is a storage entity
                    StorageEntity se = (StorageEntity) e.entityData;

                    System.out.println("CLOSED EVENT");

                    se.onInventoryClosed();
                }
            }
        }
    }

}
