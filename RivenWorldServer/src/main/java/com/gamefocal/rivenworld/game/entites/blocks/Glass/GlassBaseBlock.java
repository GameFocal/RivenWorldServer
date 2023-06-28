package com.gamefocal.rivenworld.game.entites.blocks.Glass;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public abstract class GlassBaseBlock<T> extends Block<T> {

    public GlassBaseBlock() {
        this.setHealth(25);
        this.setMaxHealth(25);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if (Pickaxe.class.isAssignableFrom(inHand.getClass())) {
            return 1.25f;
        }

        return .05f;
    }
}