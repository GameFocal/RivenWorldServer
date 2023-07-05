package com.gamefocal.rivenworld.game.items.clothes.feet.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SimpleIronBoots_R;

public class SimpleIronBoots extends PlayerShoesItem {

    public SimpleIronBoots() {
        super();
        this.name = "Simple Iron Boots";
        this.desc = "Boots made form iron and leather";
        this.icon = InventoryDataRow.Boots2_Medium_T4;
        this.mesh = InventoryDataRow.Boots2_Medium_T4;
        this.initDurability(75);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleIronBoots_R();
    }

    @Override
    public float defend() {
        return 5;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "SimpleIronBoots";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
