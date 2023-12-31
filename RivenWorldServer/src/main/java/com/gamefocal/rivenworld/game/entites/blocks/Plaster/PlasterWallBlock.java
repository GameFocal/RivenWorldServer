package com.gamefocal.rivenworld.game.entites.blocks.Plaster;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class PlasterWallBlock extends PlasterBaseBlock<PlasterWallBlock> {

    public PlasterWallBlock() {
        super();
        this.type = "Plaster_Wall";
        this.initHealth(super.maxHealth/4);
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
