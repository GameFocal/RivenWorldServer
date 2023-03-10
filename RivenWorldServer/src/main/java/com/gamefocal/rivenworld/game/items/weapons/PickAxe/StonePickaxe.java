package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.Weapons.StonePickaxeRecipe;

public class StonePickaxe extends Pickaxe implements InventoryCraftingInterface {

    public StonePickaxe() {
        this.isEquipable = true;
        this.icon = InventoryDataRow.Stone_Pickaxe;
        this.mesh = InventoryDataRow.Stone_Pickaxe;
        this.name = "Stone Pickaxe";
        this.desc = "A pickaxe made of Stone";
    }

    @Override
    public float hit() {
        return 10;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StonePickaxeRecipe();
    }
}
