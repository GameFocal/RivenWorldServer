package com.gamefocal.rivenworld.game.items.clothes.legs.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.SimpleIronLegs_R;

public class SimpleIronLegs extends PlayerLegItem {

    public SimpleIronLegs() {
        this.name = "Simple Iron Pants";
        this.desc = "A pants made of iron, leather and cloth";
        this.icon = InventoryDataRow.Pants_Heavy_T3_Dark;
        this.mesh = InventoryDataRow.Pants_Heavy_T3_Dark;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleIronLegs_R();
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
        return "SimpleIronLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
