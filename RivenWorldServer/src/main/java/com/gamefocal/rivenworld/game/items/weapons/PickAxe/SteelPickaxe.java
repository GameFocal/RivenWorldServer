package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.Weapons.SteelPickaxeRecipe;

public class SteelPickaxe extends Pickaxe implements InventoryCraftingInterface {

    public SteelPickaxe() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Steel_Pickaxe";
    }

    @Override
    public float hit() {
        return 25;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelPickaxeRecipe();
    }
}
