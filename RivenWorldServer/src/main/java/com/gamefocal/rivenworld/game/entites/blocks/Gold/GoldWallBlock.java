package com.gamefocal.rivenworld.game.entites.blocks.Gold;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class GoldWallBlock extends GoldBaseBlock<GoldWallBlock> {

    public GoldWallBlock() {
        this.type = "Gold_Wall";
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
