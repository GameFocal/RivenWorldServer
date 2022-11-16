package com.gamefocal.island.game.entites.resources;

import com.gamefocal.island.game.entites.ResourceNodeEntity;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.items.WoodItem;
import org.apache.commons.lang3.RandomUtils;

public class TreeResource extends ResourceNodeEntity<TreeResource> {
    public TreeResource() {
        this.drops = new InventoryStack[]{new InventoryStack(WoodItem.class, 24)};
        this.type = "tree";

        int size = RandomUtils.nextInt(0, 3);
        int type = RandomUtils.nextInt(0, 3);

        this.setMeta("type", type);
        this.setMeta("size", size);
    }

    public TreeResource(int type, int size) {
        this.drops = new InventoryStack[]{new InventoryStack(WoodItem.class, 24)};
        this.type = "tree";
        this.setMeta("type", type);
        this.setMeta("size", size);
    }
}
