package com.gamefocal.rivenworld.game.entites.blocks.Glass;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class GlassTileBlock extends GlassBaseBlock<GlassTileBlock> {

    public GlassTileBlock() {
        super();
        this.type = "Glass_Tile";
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
