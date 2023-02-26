package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class IronNode extends ResourceNodeEntity<IronNode> {

    public IronNode() {
        this.type = "Iron-node";
//        this.drops = new InventoryStack[]{
//                new InventoryStack(new IronOre(), 5),
//                new InventoryStack(new Stone(), 5)
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
                new InventoryStack(new IronOre(), 5),
                new InventoryStack(new Stone(), 5)
        };
    }
}
