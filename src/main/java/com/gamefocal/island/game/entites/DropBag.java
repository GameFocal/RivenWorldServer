package com.gamefocal.island.game.entites;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

public class DropBag extends GameEntity<DropBag> {

    private UUID droppedBy;

    private Long droppedAt;

    public DropBag(HiveNetConnection droppedBy, InventoryStack... items) {
        this.droppedBy = droppedBy.getUuid();

        this.type = "drop-bag";

        this.droppedAt = System.currentTimeMillis();
        this.inventory = new Inventory(items);
    }

    public Long getDroppedAt() {
        return droppedAt;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public UUID getDroppedBy() {
        return droppedBy;
    }
}
