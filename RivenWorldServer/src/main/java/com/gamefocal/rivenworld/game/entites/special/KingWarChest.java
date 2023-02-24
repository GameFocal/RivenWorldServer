package com.gamefocal.rivenworld.game.entites.special;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.storage.StorageEntity;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.service.KingService;

public class KingWarChest extends StorageEntity<KingWarChest> {

    public KingWarChest() {
        this.type = "ChestPlaceable";
        this.inventory = new Inventory(InventoryType.CONTAINER, "The Thrones Warchest", "storage-chest", 250);
        this.inventory.setAttachedEntity(this.uuid);
        this.inventory.setLocked(true);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

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
        if (KingService.isTheKing != null) {
            if (connection.getPlayer().uuid.equalsIgnoreCase(KingService.isTheKing.uuid)) {
                return "[e] View The Warchest";
            }
        }

        return "Only the King can open this";
    }
}
