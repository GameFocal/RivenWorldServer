package com.gamefocal.rivenworld.game.items.clothes.feet.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.MediumIronBoots_R;

public class MediumIronBoots extends PlayerShoesItem {

    public MediumIronBoots() {
        super();
        this.name = "Medium Iron Boots";
        this.desc = "Boots made form iron and leather";
        this.icon = InventoryDataRow.Boots2_Heavy_T4;
        this.mesh = InventoryDataRow.Boots2_Heavy_T4;
        this.durability = 350;
        this.maxDurability = 350;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MediumIronBoots_R();
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
        return "MediumIronBoots";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
