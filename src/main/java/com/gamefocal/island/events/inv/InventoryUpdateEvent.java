package com.gamefocal.island.events.inv;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.game.inventory.Inventory;

public class InventoryUpdateEvent extends Event<InventoryUpdateEvent> {

    public Inventory inventory;

    public InventoryUpdateEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
