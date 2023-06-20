package com.gamefocal.rivenworld.game.items.clothes.feet.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.HeavyIronBoots_R;

public class HeavyIronBoots extends PlayerShoesItem {

    public HeavyIronBoots() {
        super();
        this.name = "Heavy Iron Boots";
        this.desc = "Boots made form iron and leather";
        this.icon = InventoryDataRow.Boots2_Medium_T6;
        this.mesh = InventoryDataRow.Boots2_Medium_T6;
        this.durability = 400;
        this.maxDurability = 400;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavyIronBoots_R();
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
        return "HeavyIronBoots";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
