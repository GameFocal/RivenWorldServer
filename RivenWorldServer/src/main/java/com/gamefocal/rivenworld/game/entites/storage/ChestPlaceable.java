package com.gamefocal.rivenworld.game.entites.storage;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryType;

public class ChestPlaceable extends StorageEntity<ChestPlaceable> {

    public ChestPlaceable() {
        this.type = "ChestPlaceable";
        this.inventory = new Inventory(InventoryType.CONTAINER, "Storage Chest", "storage-chest", 64);
        this.inventory.setAttachedEntity(this.uuid);
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
        return "[e] Open Chest";
    }
}
