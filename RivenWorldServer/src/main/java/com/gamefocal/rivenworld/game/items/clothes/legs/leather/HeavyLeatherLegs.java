package com.gamefocal.rivenworld.game.items.clothes.legs.leather;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.HeavyLeatherLegs_R;

public class HeavyLeatherLegs extends PlayerLegItem {

    public HeavyLeatherLegs() {
        this.name = "Heavy Leather Pants";
        this.desc = "A pants made of leather and cloth";
        this.icon = InventoryDataRow.Pants_Medium_T2_1;
        this.mesh = InventoryDataRow.Pants_Medium_T2_1;
        this.initDurability(115);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavyLeatherLegs_R();
    }

    @Override
    public float defend() {
        return 15;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "HeavyLeatherLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
