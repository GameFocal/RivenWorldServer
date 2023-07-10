package com.gamefocal.rivenworld.game.entites.blocks.Iron;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class IronWallBlock extends IronBaseBlock<IronWallBlock> {

    public IronWallBlock() {
        super();
        this.type = "Iron_Wall";
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
