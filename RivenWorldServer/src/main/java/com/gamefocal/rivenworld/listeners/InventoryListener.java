package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.inv.InventoryCloseEvent;
import com.gamefocal.rivenworld.events.inv.InventoryOpenEvent;
import com.gamefocal.rivenworld.events.inv.InventoryUpdateEvent;
import com.gamefocal.rivenworld.game.entites.storage.StorageEntity;
import com.gamefocal.rivenworld.models.GameEntityModel;

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
        UUID attachedUUID = event.getInventory().getAttachedEntity();
        if (attachedUUID != null) {
            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(attachedUUID);
            if (e != null) {
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {
                    // Is a storage entity
                    StorageEntity se = (StorageEntity) e.entityData;
                    se.onInventoryClosed();
                }
            }
        }
    }

}
