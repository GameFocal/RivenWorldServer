package com.gamefocal.rivenworld.game.entites.blocks.Log;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class LogWallBlock extends Block<LogWallBlock> {

    public LogWallBlock() {
        this.type = "Log_Wall";
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

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Hatchet.class.isAssignableFrom(inHand.getClass())) {
            return 1.25f;
        }

        return .05f;
    }
}
