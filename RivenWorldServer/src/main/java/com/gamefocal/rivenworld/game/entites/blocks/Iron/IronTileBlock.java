package com.gamefocal.rivenworld.game.entites.blocks.Iron;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class IronTileBlock extends IronBaseBlock<IronTileBlock> {

    public IronTileBlock() {
        super();
        this.type = "Iron_Tile";
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
