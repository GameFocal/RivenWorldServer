package com.gamefocal.rivenworld.game.entites.blocks.Wood;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class WoodHalfBlock extends WoodBaseBlock<WoodHalfBlock> {

    public WoodHalfBlock() {
        super();
        this.type = "WoodHalfBlock";
        this.initHealth(super.maxHealth/2);
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
}
