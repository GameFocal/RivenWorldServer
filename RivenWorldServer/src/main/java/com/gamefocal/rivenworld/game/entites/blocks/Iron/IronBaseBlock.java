package com.gamefocal.rivenworld.game.entites.blocks.Iron;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public abstract class IronBaseBlock<T> extends Block<T> {

    public IronBaseBlock() {
        this.initHealth(4000);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return 1.10f;
        }

        return 1;
    }
}
