package com.gamefocal.rivenworld.game.items.clothes.chest.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.SimpleIronShirt_R;

public class SimpleIronShirt extends PlayerChestItem {

    public SimpleIronShirt() {
        this.name = "Simple Iron Shirt";
        this.desc = "A shirt made of leather and iron";
        this.icon = InventoryDataRow.Chest1_Medium_T4;
        this.mesh = InventoryDataRow.Chest1_Medium_T4;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleIronShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
