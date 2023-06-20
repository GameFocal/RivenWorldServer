package com.gamefocal.rivenworld.game.items.clothes.legs.cloth;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.SimpleClothLegs_R;

public class SimpleClothLegs extends PlayerLegItem {

    public SimpleClothLegs() {
        this.name = "Simple Cloth Pants";
        this.desc = "A pants made of cloth";
        this.icon = InventoryDataRow.Pants_Light_T1_Green;
        this.mesh = InventoryDataRow.Pants_Light_T1_Green; }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleClothLegs_R();
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
        return "SimpleClothLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
