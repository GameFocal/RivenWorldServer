package com.gamefocal.rivenworld.game.entites.blocks.Dirt;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class DirtWallBlock extends DirtBaseBlock<DirtWallBlock> {

    public DirtWallBlock() {
        super();
        this.type = "Dirt_Wall";
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
