package com.gamefocal.island.game.entites.generics;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.entites.placable.PlaceableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.sounds.GameSounds;

public abstract class DoorEntity<T> extends PlaceableEntity<T> implements InteractableEntity {

    protected InventoryItem lock;

    protected boolean isOpen = false;

    @Override
    public void onSpawn() {

    }

    @Override
    public void onSync() {
        this.setMeta("open", (this.isOpen ? "t" : "f"));
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    public InventoryItem getLock() {
        return lock;
    }

    public void setLock(InventoryItem lock) {
        this.lock = lock;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use Door [q] Lock/Unlock Door [r] Pickup";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.USE) {
            // Open the door
            // TODO: Do a permission check based on lock here.

            if (!this.isOpen) {
                // Close
                connection.playSoundAtPlayer(GameSounds.DOOR_OPEN, 1.0f, 1.0f);
            } else {
                // Open
                connection.playSoundAtPlayer(GameSounds.DOOR_OPEN, 1.0f, 1.0f);
            }

            this.isOpen = !this.isOpen;

            DedicatedServer.instance.getWorld().updateEntity(this);
        }
    }
}
