package com.gamefocal.rivenworld.game.entites.blocks.Thatch;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class ThatchTileBlock extends ThatchBaseBlock<ThatchTileBlock> {

    public ThatchTileBlock() {
        super();
        this.type = "Thatch_Tile";
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
