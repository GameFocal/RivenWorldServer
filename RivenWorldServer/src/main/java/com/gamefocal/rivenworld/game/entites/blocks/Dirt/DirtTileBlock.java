package com.gamefocal.rivenworld.game.entites.blocks.Dirt;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class DirtTileBlock extends DirtBaseBlock<DirtTileBlock> {

    public DirtTileBlock() {
        this.type = "Dirt_Tile";
        this.setHealth(super.health/4);
        this.setMaxHealth(super.maxHealth/4);
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
