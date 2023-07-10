package com.gamefocal.rivenworld.game.items.clothes.chest.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.SimpleIronShirt_R;

public class SimpleIronShirt extends PlayerChestItem {

    public SimpleIronShirt() {
        super();
        this.name = "Simple Iron Shirt";
        this.desc = "A shirt made of leather and iron";
        this.icon = InventoryDataRow.Chest1_Medium_T3;
        this.mesh = InventoryDataRow.Chest1_Medium_T3;
        this.initDurability(130);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleIronShirt_R();
    }

    @Override
    public float defend() {
        return 20;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "SimpleIronShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
