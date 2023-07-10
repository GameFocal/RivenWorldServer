package com.gamefocal.rivenworld.game.items.clothes.chest.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.MediumIronShirt_R;

public class MediumIronShirt extends PlayerChestItem {

    public MediumIronShirt() {
        super();
        this.name = "Medium Iron Shirt";
        this.desc = "A shirt made of leather and iron";
        this.icon = InventoryDataRow.Chest1_Medium_T5_Red;
        this.mesh = InventoryDataRow.Chest1_Medium_T5_Red;
        this.initDurability(150);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MediumIronShirt_R();
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
        return "MediumIronShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
