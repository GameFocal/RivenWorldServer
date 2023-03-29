package com.gamefocal.rivenworld.game.items.clothes.chest.cloth;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;

public class SimpleClothShirt extends PlayerChestItem {

    public SimpleClothShirt() {
        this.name = "Simple Cloth Shirt";
        this.desc = "A simple shirt made of cloth";
        this.icon = InventoryDataRow.Shirt0;
        this.mesh = InventoryDataRow.Shirt0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return null;
    }

    @Override
    public float defend() {
        return 1;
    }
}
