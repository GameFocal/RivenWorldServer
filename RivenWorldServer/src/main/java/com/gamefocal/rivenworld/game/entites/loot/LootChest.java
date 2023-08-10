package com.gamefocal.rivenworld.game.entites.loot;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.storage.StorageEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public abstract class LootChest extends StorageEntity<LootChest> {

    protected boolean isGenerated = false;
    protected long spawnedAt = 0L;
    protected long despawnAt = 0L;
    protected int lootTier = 0;

    public LootChest(String type, int size) {
        this.type = type;
        this.inventory = new Inventory(InventoryType.CONTAINER, "Loot Chest", "storage-chest", size);
        this.inventory.setAttachedEntity(this.uuid);
        this.initHealth(1000);
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public long getSpawnedAt() {
        return spawnedAt;
    }

    public long getDespawnAt() {
        return despawnAt;
    }

    public int getLootTier() {
        return lootTier;
    }

    @Override
    public void onInventoryUpdated() {

    }

    @Override
    public void onInventoryOpen() {
        // TODO: Generate
    }

    @Override
    public void onInventoryClosed() {
        // TODO: Delete if empty
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        // TODO: Check Skills???
        return "[e] Unlock Loot";
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 50);
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {

        /*
        * TODO: Generate loot from loot tables here :)
        * */

        super.onInteract(connection, action, inHand);
    }
}
