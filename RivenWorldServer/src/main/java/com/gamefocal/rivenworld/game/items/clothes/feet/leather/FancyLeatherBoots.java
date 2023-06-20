package com.gamefocal.rivenworld.game.items.clothes.feet.leather;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.FancyLeatherBoots_R;

public class FancyLeatherBoots extends PlayerShoesItem {

    public FancyLeatherBoots() {
        this.name = "Fancy Leather Boots";
        this.desc = "Boots made form leather and fiber";
        this.icon = InventoryDataRow.Boots2_Heavy_T2;
        this.mesh = InventoryDataRow.Boots2_Heavy_T2;
        this.durability = 200;
        this.maxDurability = 200;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FancyLeatherBoots_R();
    }

    @Override
    public float defend() {
        return 25;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "FancyLeatherBoots";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
