package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;

public class DirtNode extends ResourceNodeEntity<DirtNode> {

    public DirtNode() {
        this.type = "Dirt-node";
//        this.drops = new InventoryStack[]{
//                new InventoryStack(new DirtBlockItem(), 15)
//        };
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
    public InventoryStack[] drops() {
        return new InventoryStack[]{
                new InventoryStack(new DirtBlockItem(), 15)
        };
    }
}
