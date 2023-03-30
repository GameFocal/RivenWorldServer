package com.gamefocal.rivenworld.game.items.clothes.head;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerHeadItem;
import com.gamefocal.rivenworld.game.recipes.clothing.head.ClothCap_R;

public class ClothCap extends PlayerHeadItem {

    public ClothCap() {
        this.name = "Cloth Cap";
        this.desc = "A piece of cloth headware";
        this.icon = InventoryDataRow.Helm0_Bsmth_T1;
        this.mesh = InventoryDataRow.Helm0_Bsmth_T1;
    }

    @Override
    public float defend() {
        return 1;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClothCap_R();
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "ClothCap";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
