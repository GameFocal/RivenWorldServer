package com.gamefocal.island.game.entites.resources;

import com.gamefocal.island.game.entites.ResourceNodeEntity;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.items.WoodItem;

public class TreeResource extends ResourceNodeEntity<TreeResource> {
    public TreeResource() {
        this.drops = new InventoryStack[]{new InventoryStack(WoodItem.class, 24)};
    }
}
