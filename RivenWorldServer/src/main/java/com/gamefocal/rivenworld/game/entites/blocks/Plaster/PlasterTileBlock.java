package com.gamefocal.rivenworld.game.entites.blocks.Plaster;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class PlasterTileBlock extends PlasterBaseBlock<PlasterTileBlock> {

    public PlasterTileBlock() {
        super();
        this.type = "Plaster_Tile";
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
