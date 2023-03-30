package com.gamefocal.rivenworld.game.items.clothes.legs.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.SimpleLeatherLegs_R;

public class SimpleLeatherLegs extends PlayerLegItem {

    public SimpleLeatherLegs() {
        this.name = "Simple Leather Pants";
        this.desc = "A pants made of leather and cloth";
        this.icon = InventoryDataRow.Pants_Heavy_T1_Dark;
        this.mesh = InventoryDataRow.Pants_Heavy_T1_Dark;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleLeatherLegs_R();
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
        return "SimpleLeatherLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
