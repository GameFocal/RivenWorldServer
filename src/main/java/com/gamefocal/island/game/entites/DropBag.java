package com.gamefocal.island.game.entites;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;

public class DropBag extends GameEntity<DropBag> {

    private Long droppedAt;

    private Inventory inventory;

    public DropBag(InventoryStack... items) {

        this.type = "drop-bag";

        this.droppedAt = System.currentTimeMillis();
        this.inventory = new Inventory(items);

        this.setMeta("container", true);
    }

    public Long getDroppedAt() {
        return droppedAt;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
