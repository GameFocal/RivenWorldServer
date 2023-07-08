package com.gamefocal.rivenworld.game.entites.blocks.Gold;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public abstract class GoldBaseBlock<T> extends Block<T> {

    public GoldBaseBlock() {
        this.initHealth(450);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return 1.10f;
        }

        return 1;
    }
}
