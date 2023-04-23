package com.gamefocal.rivenworld.game.entites.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public abstract class StoneBrickBaseBlock<T> extends Block<T> {

    public StoneBrickBaseBlock() {
        this.health = 250;
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return .5f;
        }

        return 0f;
    }
}
