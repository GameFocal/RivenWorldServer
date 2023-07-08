package com.gamefocal.rivenworld.game.entites.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class StoneBrickTileBlock extends StoneBrickBaseBlock<StoneBrickTileBlock> {

    public StoneBrickTileBlock() {
        super();
        this.type = "Brick_Tile";
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
