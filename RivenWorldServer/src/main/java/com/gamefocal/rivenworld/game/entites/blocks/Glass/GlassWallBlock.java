package com.gamefocal.rivenworld.game.entites.blocks.Glass;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class GlassWallBlock extends GlassBaseBlock<GlassWallBlock> {

    public GlassWallBlock() {
        super();
        this.type = "Glass_Wall";
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
