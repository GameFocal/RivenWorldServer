package com.gamefocal.rivenworld.game.items.clothes.feet.leather;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.FancyLeatherShoes_R;

public class FancyLeatherShoes extends PlayerShoesItem {

    public FancyLeatherShoes() {
        this.name = "Fancy Leather Shoes";
        this.desc = "Shoes made form leather and fiber";
        this.icon = InventoryDataRow.Boots1_Light_T5;
        this.mesh = InventoryDataRow.Boots1_Light_T5;
        this.initDurability(60);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FancyLeatherShoes_R();
    }

    @Override
    public float defend() {
        return 15;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "FancyLeatherShoes";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
