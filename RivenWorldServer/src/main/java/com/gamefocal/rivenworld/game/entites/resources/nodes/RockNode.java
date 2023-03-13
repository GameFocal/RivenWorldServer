package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class RockNode extends ResourceNodeEntity<RockNode> {

    public RockNode() {
        this.type = "rock-node";
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
                new InventoryStack(new Stone(), 5)
        };
    }
}
