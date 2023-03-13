package com.gamefocal.rivenworld.game.entites.resources.nodes;

import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.items.weapons.Spade;

public class GravelNode extends ResourceNodeEntity<GravelNode> {

    public GravelNode() {
        this.type = "gravel-node";
        this.allowedTools.add(Spade.class);
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
        return new InventoryStack[0];
    }
}
