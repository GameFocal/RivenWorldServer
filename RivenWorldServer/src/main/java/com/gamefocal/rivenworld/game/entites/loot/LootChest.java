package com.gamefocal.rivenworld.game.entites.loot;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.storage.StorageEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public abstract class LootChest extends StorageEntity<LootChest> {

    protected boolean isGenerated = false;
    protected long spawnedAt = 0L;
    protected long despawnAt = 0L;
    protected int lootTier = 0;
    protected boolean playSongOnClose = true;

    public LootChest(String type, int size) {
        this.type = type;
        this.inventory = new Inventory(InventoryType.CONTAINER, "Loot Chest", "storage-chest", size);
        this.inventory.setAttachedEntity(this.uuid);
        this.inventory.setLocked(true);
        this.initHealth(1000);
    }

    public boolean isPlaySongOnClose() {
        return playSongOnClose;
    }

    public void setPlaySongOnClose(boolean playSongOnClose) {
        this.playSongOnClose = playSongOnClose;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public long getSpawnedAt() {
        return spawnedAt;
    }

    public void setSpawnedAt(long spawnedAt) {
        this.spawnedAt = spawnedAt;
    }

    public long getDespawnAt() {
        return despawnAt;
    }

    public void setDespawnAt(long despawnAt) {
        this.despawnAt = despawnAt;
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
        if (this.inventory.isEmpty()) {
            if (playSongOnClose) {
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.LEVEL_UP, this.location, 1500, 1, 1);
            }
            DedicatedServer.instance.getWorld().despawn(this.uuid);
        }
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
        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.CHEST_OPEN, this.location, 5000, 1, 1);

        super.onInteract(connection, action, inHand);
    }
}
