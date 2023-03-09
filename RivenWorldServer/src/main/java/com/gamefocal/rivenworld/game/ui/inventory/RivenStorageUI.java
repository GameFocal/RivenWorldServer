package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.inv.InventoryCloseEvent;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.recipes.Placeables.CampFirePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.WorkBenchPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.StoneHatchetRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.WoodenClubRecipe;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.InventoryService;
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
