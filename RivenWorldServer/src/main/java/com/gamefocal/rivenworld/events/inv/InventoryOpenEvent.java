package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;

public class InventoryOpenEvent extends Event<InventoryOpenEvent> {

    private Inventory inventory;

    private HiveNetConnection connection;

    public InventoryOpenEvent(Inventory inventory, HiveNetConnection connection) {
        this.inventory = inventory;
        this.connection = connection;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
