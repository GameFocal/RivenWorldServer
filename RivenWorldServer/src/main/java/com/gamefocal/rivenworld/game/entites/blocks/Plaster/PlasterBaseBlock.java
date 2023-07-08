package com.gamefocal.rivenworld.game.entites.blocks.Plaster;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public abstract class PlasterBaseBlock<T> extends Block<T> {

    public PlasterBaseBlock() {
        this.initHealth(120);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Hatchet.class.isAssignableFrom(inHand.getClass())) {
            return 1.10f;
        }

        return 1;
    }
}
