package com.gamefocal.rivenworld.game.entites.storage;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryType;

public class SimpleChestPlaceable extends StorageEntity<SimpleChestPlaceable> {

    public SimpleChestPlaceable() {
        this.type = "Wooden_Crate_04";
        this.inventory = new Inventory(InventoryType.CONTAINER, "Wooden Crate", "storage-chest", 6);
        this.inventory.setAttachedEntity(this.uuid);
        this.health = 100;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onInventoryUpdated() {

    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInventoryClosed() {

    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Open Crate";
    }
}
