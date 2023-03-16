package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class GoldNode extends ResourceNodeEntity<GoldNode> {

    public GoldNode() {
        this.type = "Gold-node";
        this.allowedTools.add(Pickaxe.class);
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
                new InventoryStack(new GoldOre(), 3)
//                new InventoryStack(new Stone(), 5)
        };
    }
}
