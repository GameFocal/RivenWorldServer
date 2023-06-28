package com.gamefocal.rivenworld.game.entites.storage;

import com.badlogic.gdx.math.Vector2;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.DisposableEntity;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.UUID;

public class DropBag extends StorageEntity<DropBag> implements DisposableEntity {

    private UUID droppedBy;

    private Long droppedAt;

    public DropBag(HiveNetConnection droppedBy, InventoryStack... items) {
        if (droppedBy != null) {
            this.droppedBy = droppedBy.getUuid();
        }

        this.type = "drop-bag";

        this.droppedAt = System.currentTimeMillis();
        this.inventory = new Inventory(InventoryType.CONTAINER, "Dropped Items", "drop", items);
//        this.inventory.setAttachedEntity(this.uuid);
        this.inventory.setLocked(true);
    }

    public DropBag(HiveNetConnection droppedBy) {
        this.droppedBy = droppedBy.getUuid();
        this.type = "drop-bag";
        this.inventory = new Inventory(InventoryType.CONTAINER, "Dropped Items", "drop",1);
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
        this.viewing = null;
        if (this.inventory.isEmpty()) {

            System.out.println("DESPAWN");

            DedicatedServer.instance.getWorld().despawn(this.uuid);
        } else {
//            System.err.println("Is not empty");
        }
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.viewing != null) {
            return "Someone is Viewing";
        }

        return "[e] View Contents";
    }

    public UUID getDroppedBy() {
        return droppedBy;
    }

    @Override
    public void onDespawn() {

    }
}
