package com.gamefocal.rivenworld.game.entites.storage;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class ChestPlaceable extends StorageEntity<ChestPlaceable> {

    public ChestPlaceable() {
        this.type = "ChestPlaceable";
        this.inventory = new Inventory(InventoryType.CONTAINER, "Storage Chest", "storage-chest", 24);
        this.inventory.setAttachedEntity(this.uuid);
        this.initHealth(500);
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

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 50);
    }
}
