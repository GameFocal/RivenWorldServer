package com.gamefocal.rivenworld.game.items.clothes.chest.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.SimpleLeatherShirt_R;

public class SimpleLeatherShirt extends PlayerChestItem {

    public SimpleLeatherShirt() {
        this.name = "Simple Leather Shirt";
        this.desc = "A shirt made of leather and cloth";
        this.icon = InventoryDataRow.Chest1_Medium_T1;
        this.mesh = InventoryDataRow.Chest1_Medium_T1;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleLeatherShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
