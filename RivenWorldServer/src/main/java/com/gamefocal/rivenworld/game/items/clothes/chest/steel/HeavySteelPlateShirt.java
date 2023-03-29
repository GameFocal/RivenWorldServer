package com.gamefocal.rivenworld.game.items.clothes.chest.steel;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.HeavySteelPlateShirt_R;

public class HeavySteelPlateShirt extends PlayerChestItem {

    public HeavySteelPlateShirt() {
        this.name = "Heavy Steel Plate Shirt";
        this.desc = "A shirt made of steel plate";
        this.icon = InventoryDataRow.Chest1_Heavy_T5;
        this.mesh = InventoryDataRow.Chest1_Heavy_T5;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavySteelPlateShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
