package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.SaveService;
import com.google.gson.JsonObject;

public class RivenStorageUI extends GameUI<Inventory> {

    public RivenStorageUI() {
        this.focus = false;
    }

    @Override
    public String name() {
        return "storage";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {

        if (obj.getAttachedEntity() != null) {
            GameEntityModel attached = DedicatedServer.instance.getWorld().getEntityFromId(obj.getAttachedEntity());

            SaveService.queueForSave(attached);

            if (EntityStorageInterface.class.isAssignableFrom(attached.entityData.getClass())) {
                // Is a storage entity
                EntityStorageInterface storageInterface = (EntityStorageInterface) attached.entityData;
                storageInterface.onInventoryUpdated();
            }
        }

        connection.updatePlayerInventory();

        JsonObject o = new JsonObject();
        o.add("container", obj.toJson());

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object);
        object.attachToUI(this);
        connection.getPlayer().inventory.attachToUI(this);

        if (object.getAttachedEntity() != null) {
            GameEntityModel attached = DedicatedServer.instance.getWorld().getEntityFromId(object.getAttachedEntity());

            if (EntityStorageInterface.class.isAssignableFrom(attached.entityData.getClass())) {
                // Is a storage entity
                EntityStorageInterface storageInterface = (EntityStorageInterface) attached.entityData;
                storageInterface.onInventoryOpen();
            }
        }

    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        if (object.getAttachedEntity() != null) {
            GameEntityModel attached = DedicatedServer.instance.getWorld().getEntityFromId(object.getAttachedEntity());

            if (EntityStorageInterface.class.isAssignableFrom(attached.entityData.getClass())) {
                // Is a storage entity
                EntityStorageInterface storageInterface = (EntityStorageInterface) attached.entityData;
                storageInterface.onInventoryClosed();
            }
        }

        object.detachFromUI(this);
        connection.getPlayer().inventory.detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        }
    }
}
