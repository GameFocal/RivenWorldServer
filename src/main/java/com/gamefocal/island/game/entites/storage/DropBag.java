package com.gamefocal.island.game.entites.storage;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.game.tasks.HiveDelayedTask;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.UUID;

public class DropBag extends StorageEntity<DropBag> {

    private UUID droppedBy;

    private Long droppedAt;

    public DropBag(HiveNetConnection droppedBy, InventoryStack... items) {
        this.droppedBy = droppedBy.getUuid();

        this.type = "drop-bag";

        this.droppedAt = System.currentTimeMillis();
        this.inventory = new Inventory(InventoryType.CONTAINER, "Dropped Items", "drop", items);
        this.inventory.setLocked(true);
    }

    public Long getDroppedAt() {
        return droppedAt;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onInventoryUpdated() {
    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInventoryClosed() {
        System.out.println("CLOSED");
        if (this.inventory.isEmpty()) {
            TaskService.scheduledDelayTask(() -> {
                System.out.println("Despawn empty drop bag...");
                DedicatedServer.instance.getWorld().despawn(this.uuid);
            }, 15L, false);
        }
    }

    public UUID getDroppedBy() {
        return droppedBy;
    }
}
