package com.gamefocal.rivenworld.game.entites.blocks.Wood;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class WoodTileBlock extends WoodBaseBlock<WoodTileBlock> {

    public WoodTileBlock() {
        this.type = "Wood_Tile";
        this.health = super.health/4;
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
