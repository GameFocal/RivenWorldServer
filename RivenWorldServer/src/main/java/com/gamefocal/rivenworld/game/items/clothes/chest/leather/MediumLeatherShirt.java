package com.gamefocal.rivenworld.game.items.clothes.chest.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.MediumLeatherShirt_R;

public class MediumLeatherShirt extends PlayerChestItem {

    public MediumLeatherShirt() {
        this.name = "Medium Leather Shirt";
        this.desc = "A shirt made of leather and cloth";
        this.icon = InventoryDataRow.Chest1_Medium_T2;
        this.mesh = InventoryDataRow.Chest1_Medium_T2;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MediumLeatherShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
