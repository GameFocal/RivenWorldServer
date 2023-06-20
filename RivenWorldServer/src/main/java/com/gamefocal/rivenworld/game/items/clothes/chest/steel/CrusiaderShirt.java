package com.gamefocal.rivenworld.game.items.clothes.chest.steel;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.CrusaiderShirt_R;

public class CrusiaderShirt extends PlayerChestItem {

    public CrusiaderShirt() {
        super();
        this.name = "Crusader Shirt";
        this.desc = "A shirt worn by a Crusader";
        this.icon = InventoryDataRow.Chest1_Crus_T5;
        this.mesh = InventoryDataRow.Chest1_Crus_T5;
        this.durability = 500;
        this.maxDurability = 500;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CrusaiderShirt_R();
    }

    @Override
    public float defend() {
        return 40;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "CrusiaderShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
