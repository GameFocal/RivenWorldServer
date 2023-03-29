package com.gamefocal.rivenworld.game.items.clothes.chest.cloth;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;

public class FancyClothShirt extends PlayerChestItem {

    public FancyClothShirt() {
        this.name = "Fancy Cloth Shirt";
        this.desc = "A nicer shirt made of cloth";
        this.icon = InventoryDataRow.Shirt1;
        this.mesh = InventoryDataRow.Shirt1;
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
