package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.game.inventory.Inventory;

public class InventoryUpdateEvent extends Event<InventoryUpdateEvent> {

    public Inventory inventory;

    public InventoryUpdateEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
