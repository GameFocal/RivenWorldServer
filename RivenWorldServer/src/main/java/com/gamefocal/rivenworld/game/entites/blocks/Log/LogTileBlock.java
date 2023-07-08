package com.gamefocal.rivenworld.game.entites.blocks.Log;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class LogTileBlock extends LogBaseBlock<LogTileBlock> {

    public LogTileBlock() {
        super();
        this.type = "Log_Tile";
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
