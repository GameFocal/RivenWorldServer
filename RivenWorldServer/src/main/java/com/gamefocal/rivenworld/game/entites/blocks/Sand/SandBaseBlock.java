package com.gamefocal.rivenworld.game.entites.blocks.Sand;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Spade;

public abstract class SandBaseBlock<T> extends Block<T> {

    public SandBaseBlock() {
        this.initHealth(40);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Spade.class.isAssignableFrom(inHand.getClass())) {
            return 1.10f;
        }

        return 1;
    }
}
