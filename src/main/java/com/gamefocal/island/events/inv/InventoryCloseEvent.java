package com.gamefocal.island.events.inv;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.Inventory;

public class InventoryCloseEvent extends Event<InventoryCloseEvent> {

    private Inventory inventory;

    private HiveNetConnection connection;

    public InventoryCloseEvent(Inventory inventory, HiveNetConnection connection) {
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
