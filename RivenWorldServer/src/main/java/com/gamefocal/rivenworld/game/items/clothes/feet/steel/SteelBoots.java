package com.gamefocal.rivenworld.game.items.clothes.feet.steel;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SteelBoots_R;

public class SteelBoots extends PlayerShoesItem {

    public SteelBoots() {
        this.name = "Steel Plate Boots";
        this.desc = "Boots made form steel plate armor";
        this.icon = InventoryDataRow.Boots2_Heavy_T6;
        this.mesh = InventoryDataRow.Boots2_Heavy_T6;
        this.durability = 650;
        this.maxDurability = 650;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelBoots_R();
    }

    @Override
    public float defend() {
        return 75;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "SteelBoots";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
