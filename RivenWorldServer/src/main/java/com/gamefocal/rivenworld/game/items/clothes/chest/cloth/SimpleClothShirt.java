package com.gamefocal.rivenworld.game.items.clothes.chest.cloth;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.SimpleClothShirt_R;

public class SimpleClothShirt extends PlayerChestItem {

    public SimpleClothShirt() {
        super();
        this.name = "Simple Cloth Shirt";
        this.desc = "A simple shirt made of cloth";
        this.icon = InventoryDataRow.Shirt0;
        this.mesh = InventoryDataRow.Shirt0;
        this.initDurability(50);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleClothShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "Chest1_T1";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
