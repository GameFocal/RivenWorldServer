package com.gamefocal.rivenworld.game.entites.blocks.Thatch;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public abstract class ThatchBaseBlock<T> extends Block<T> {

    public ThatchBaseBlock() {
        this.health = 25;
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Hatchet.class.isAssignableFrom(inHand.getClass())) {
            return 2f;
        }

        return .05f;
    }
}
