package com.gamefocal.island.game.inventory.hotbar;

import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.models.PlayerModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerHotbar implements Serializable {
    public UUID[] items = new UUID[5];

    public void linkToSlot(InventoryStack stack, int hotbarIndex) {
        this.items[hotbarIndex] = stack.getItem().getItemUUID();
    }

    public void moveToSlot(int from, int to) {
        UUID fromUUID = this.items[from];
        this.items[to] = fromUUID;
        this.items[from] = null;
    }

    public boolean isLinked(int index) {
        return (this.items[index] != null);
    }

    public void unlinkToSlot(int hotbar) {
        this.items[hotbar] = null;
    }

    public UUID getItemUUIDFromSlot(int slot) {
        return this.items[slot];
    }

    public InventoryStack getStackAtIndex(int index, PlayerModel playerModel) {
        UUID uuid = this.getItemUUIDFromSlot(index);
        if (uuid != null) {
            // Is set
            return playerModel.findStackFromUUID(uuid);
        }

        return null;
    }

}
