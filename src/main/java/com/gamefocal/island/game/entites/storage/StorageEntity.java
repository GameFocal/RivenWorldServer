package com.gamefocal.island.game.entites.storage;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.entites.generics.EntityStorageInterface;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.ui.inventory.StorageInventoryUI;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class StorageEntity<T> extends GameEntity<T> implements InteractableEntity, EntityStorageInterface {

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
        this.inventory.setAttachedEntity(this.uuid);
        DedicatedServer.get(InventoryService.class).trackInventory(this.inventory);
    }

    @Override
    public void onDespawn() {

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
            if (!this.inventory.hasOwner()) {
                try {
                    this.inventory.takeOwnership(connection, true);
                    connection.getPlayer().inventory.takeOwnership(connection, true);
                } catch (InventoryOwnedAlreadyException e) {
                    e.printStackTrace();
                }

                StorageInventoryUI ui = new StorageInventoryUI();
                ui.open(connection, this.inventory);
            }
        }
    }

}
