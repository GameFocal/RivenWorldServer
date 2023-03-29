package com.gamefocal.rivenworld.game.items.clothes.legs.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.MediumIronLegs_R;

public class MediumIronLegs extends PlayerLegItem {

    public MediumIronLegs() {
        this.name = "Medium Iron Pants";
        this.desc = "A pants made of iron, leather and cloth";
        this.icon = InventoryDataRow.Pants_Heavy_T5_Dark;
        this.mesh = InventoryDataRow.Pants_Heavy_T5_Dark;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MediumIronLegs_R();
    }

    @Override
    public float defend() {
        return 1;
    }
}
