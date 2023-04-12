package com.gamefocal.rivenworld.game.entites.blocks;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class ClayBlock extends Block<ClayBlock> {

    public ClayBlock() {
        this.type = "ClayBlock";
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

        return .25f;
    }
}
