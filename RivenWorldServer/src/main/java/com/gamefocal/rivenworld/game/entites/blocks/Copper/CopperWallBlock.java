package com.gamefocal.rivenworld.game.entites.blocks.Copper;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class CopperWallBlock extends CopperBaseBlock<CopperWallBlock> {

    public CopperWallBlock() {
        super();
        this.type = "Copper_wall";
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
