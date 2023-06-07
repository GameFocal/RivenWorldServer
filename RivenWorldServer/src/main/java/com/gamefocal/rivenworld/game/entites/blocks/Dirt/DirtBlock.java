package com.gamefocal.rivenworld.game.entites.blocks.Dirt;

import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;

public class DirtBlock extends Block<DirtBlock> {

    public DirtBlock() {
        this.type = "DirtBlock";
        this.health = 25;
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
            return 0.25f;
        }

        return 1;
    }
}
