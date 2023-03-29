package com.gamefocal.rivenworld.game.items.clothes.feet.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.Clothing.Feet.SimpleLeatherBoots_R;

public class SimpleLeatherBoots extends PlayerShoesItem {

    public SimpleLeatherBoots() {
        this.name = "Leather Boots";
        this.desc = "Boots made form leather and fiber";
        this.icon = InventoryDataRow.Boots2_Heavy_T1;
        this.mesh = InventoryDataRow.Boots2_Heavy_T1;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleLeatherBoots_R();
    }

    @Override
    public float defend() {
        return 5;
    }
}
