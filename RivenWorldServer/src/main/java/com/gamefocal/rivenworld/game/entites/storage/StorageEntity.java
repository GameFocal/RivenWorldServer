package com.gamefocal.rivenworld.game.entites.storage;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.inventory.RivenStorageUI;
import com.gamefocal.rivenworld.service.InventoryService;

public abstract class StorageEntity<T> extends PlaceableEntity<T> implements InteractableEntity, EntityStorageInterface {

    protected Inventory inventory;

    protected transient HiveNetConnection viewing = null;

    @Override
    public void onSync() {
        if (this.inventory != null) {
            this.setMeta("invid", this.inventory.getUuid().toString());
//            this.setMeta("inv", Base64.getEncoder().encodeToString(InventoryUtil.inventoryToJson(this.inventory).toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Override
    public void onHash(StringBuilder builder) {
        builder.append(this.inventory.toJson());
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
        this.inventory.setAttachedEntity(this.uuid);
//        DedicatedServer.get(InventoryService.class).trackInventory(this.inventory);
    }

    @Override
    public void onDespawn() {
//        if (!this.inventory.isEmpty()) {
//            DedicatedServer.get(InventoryService.class).dropBagAtLocation(null, inventory, this.location);
//        }
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Open Chest";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            // Use the bag
            if (this.viewing == null) {
                RivenStorageUI storageUI = new RivenStorageUI();
                storageUI.open(connection, this.inventory);
            }
        }
    }
}
