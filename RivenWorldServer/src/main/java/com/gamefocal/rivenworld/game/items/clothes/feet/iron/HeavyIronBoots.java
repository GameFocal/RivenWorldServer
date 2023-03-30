package com.gamefocal.rivenworld.game.items.clothes.feet.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;

public class HeavyIronBoots extends PlayerShoesItem {

    public HeavyIronBoots() {
        this.name = "Heavy Iron Boots";
        this.desc = "Boots made form iron and leather";
        this.icon = InventoryDataRow.Boots2_Medium_T6;
        this.mesh = InventoryDataRow.Boots2_Medium_T6;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return null;
    }

    @Override
    public float defend() {
        return 25;
    }
}