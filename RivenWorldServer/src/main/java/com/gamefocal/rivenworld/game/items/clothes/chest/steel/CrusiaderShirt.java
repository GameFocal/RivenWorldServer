package com.gamefocal.rivenworld.game.items.clothes.chest.steel;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.CrusaiderShirt_R;

public class CrusiaderShirt extends PlayerChestItem {

    public CrusiaderShirt() {
        this.name = "Crusader Shirt";
        this.desc = "A shirt worn by a Crusader";
        this.icon = InventoryDataRow.Chest1_Crus_T5;
        this.mesh = InventoryDataRow.Chest1_Crus_T5;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CrusaiderShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
