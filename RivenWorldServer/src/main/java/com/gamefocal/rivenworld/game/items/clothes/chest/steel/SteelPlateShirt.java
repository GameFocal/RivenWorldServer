package com.gamefocal.rivenworld.game.items.clothes.chest.steel;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.SteelPlateShirt_R;

public class SteelPlateShirt extends PlayerChestItem {

    public SteelPlateShirt() {
        super();
        this.name = "Steel Plate Shirt";
        this.desc = "A shirt made of steel plate";
        this.icon = InventoryDataRow.Chest1_Bsmth_T2;
        this.mesh = InventoryDataRow.Chest1_Bsmth_T2;
        this.durability = 550;
        this.maxDurability = 550;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelPlateShirt_R();
    }

    @Override
    public float defend() {
        return 45;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "SteelPlateShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
