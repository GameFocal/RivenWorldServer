package com.gamefocal.rivenworld.game.entites.blocks;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class CopperBlock extends Block<CopperBlock> {

    public CopperBlock() {
        this.type = "CopperBlock";
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
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return 1.25f;
        }

        return .05f;
    }
}
