package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.Weapons.IronPickaxeRecipe;

public class IronPickaxe extends Pickaxe implements InventoryCraftingInterface {

    public IronPickaxe() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Iron_Pickaxe";
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
        return new IronPickaxeRecipe();
    }
}
