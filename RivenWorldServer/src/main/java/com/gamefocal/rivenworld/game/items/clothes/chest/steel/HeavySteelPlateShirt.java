package com.gamefocal.rivenworld.game.items.clothes.chest.steel;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.HeavySteelPlateShirt_R;

public class HeavySteelPlateShirt extends PlayerChestItem {

    public HeavySteelPlateShirt() {
        super();
        this.name = "Heavy Steel Plate Shirt";
        this.desc = "A shirt made of steel plate";
        this.icon = InventoryDataRow.Chest1_Heavy_T5;
        this.mesh = InventoryDataRow.Chest1_Heavy_T5;
        this.initDurability(200);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavySteelPlateShirt_R();
    }

    @Override
    public float defend() {
        return 50;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "HeavySteelPlateShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
