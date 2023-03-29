package com.gamefocal.rivenworld.game.items.clothes.feet.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;

public class SimpleIronBoots extends PlayerShoesItem {

    public SimpleIronBoots() {
        this.name = "Simple Iron Boots";
        this.desc = "Boots made form iron and leather";
        this.icon = InventoryDataRow.Boots2_Medium_T4;
        this.mesh = InventoryDataRow.Boots2_Medium_T4;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return null;
    }

    @Override
    public float defend() {
        return 5;
    }
}
