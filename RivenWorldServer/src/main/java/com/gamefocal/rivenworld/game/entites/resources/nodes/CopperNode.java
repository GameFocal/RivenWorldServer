package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class CopperNode extends ResourceNodeEntity<CopperNode> {

    public CopperNode() {
        this.type = "copper-node";
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
                new InventoryStack(new CopperIgnot(), 15)
        };
    }
}
