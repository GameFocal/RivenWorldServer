package com.gamefocal.rivenworld.game.items.clothes.legs.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.MediumLeatherLegs_R;

public class MediumLeatherLegs extends PlayerLegItem {

    public MediumLeatherLegs() {
        this.name = "Medium Leather Pants";
        this.desc = "A pants made of leather and cloth";
        this.icon = InventoryDataRow.Pants_Heavy_T1;
        this.mesh = InventoryDataRow.Pants_Heavy_T1;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MediumLeatherLegs_R();
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
        return "MediumLeatherLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
