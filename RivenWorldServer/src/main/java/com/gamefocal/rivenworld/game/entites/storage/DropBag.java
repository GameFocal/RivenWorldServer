package com.gamefocal.rivenworld.game.entites.storage;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;

public class DropBag extends StorageEntity<DropBag> {

    private UUID droppedBy;

    private Long droppedAt;

    public DropBag(HiveNetConnection droppedBy, InventoryStack... items) {
        this.droppedBy = droppedBy.getUuid();

        this.type = "drop-bag";

        this.droppedAt = System.currentTimeMillis();
        this.inventory = new Inventory(InventoryType.CONTAINER, "Dropped Items", "drop", items);
        this.inventory.setAttachedEntity(this.uuid);
        this.inventory.setHasOnOff(true);
        this.inventory.setLocked(true);
    }

    public DropBag(HiveNetConnection droppedBy) {
        this.droppedBy = droppedBy.getUuid();
        this.type = "drop-bag";
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
        this.inventory.releaseOwnership();
        if (this.inventory.isEmpty()) {
            TaskService.scheduledDelayTask(() -> {
                System.out.println("Despawn empty drop bag...");
                DedicatedServer.instance.getWorld().despawn(this.uuid);
            }, 15L, false);
        }
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.inventory.hasOwner()) {
            return "Someone is Viewing";
        }

        return "[e] View Contents";
    }

    public UUID getDroppedBy() {
        return droppedBy;
    }
}
