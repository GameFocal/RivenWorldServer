package com.gamefocal.rivenworld.game.entites.blocks.Stone;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public abstract class StoneBaseBlock<T> extends Block<T> {

    public StoneBaseBlock() {
        this.health = 100;
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return .25f;
        }

        return 0f;
    }
}
