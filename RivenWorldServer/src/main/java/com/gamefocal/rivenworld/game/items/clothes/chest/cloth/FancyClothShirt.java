package com.gamefocal.rivenworld.game.items.clothes.chest.cloth;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.FancyClothShirt_R;

public class FancyClothShirt extends PlayerChestItem {

    public FancyClothShirt() {
        this.name = "Fancy Cloth Shirt";
        this.desc = "A nicer shirt made of cloth";
        this.icon = InventoryDataRow.Shirt1;
        this.mesh = InventoryDataRow.Shirt1;
        this.initDurability(55);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FancyClothShirt_R();
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
        return "FancyClothShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
