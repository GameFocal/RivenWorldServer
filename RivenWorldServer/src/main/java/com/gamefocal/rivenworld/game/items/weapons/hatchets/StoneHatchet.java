package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.Weapons.StoneHatchetRecipe;

public class StoneHatchet extends Hatchet implements InventoryCraftingInterface {

    public StoneHatchet() {
        this.isEquipable = true;
        this.icon = InventoryDataRow.Stone_Hatchet;
        this.mesh = InventoryDataRow.Stone_Hatchet;
    }

    @Override
    public float hit() {
        return 5;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneHatchetRecipe();
    }
}
