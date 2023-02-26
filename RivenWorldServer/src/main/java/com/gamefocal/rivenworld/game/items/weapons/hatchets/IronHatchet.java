package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.Weapons.IronHatchetRecipe;

public class IronHatchet extends Hatchet implements InventoryCraftingInterface {

    public IronHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Iron_Hatchet";
    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronHatchetRecipe();
    }
}
