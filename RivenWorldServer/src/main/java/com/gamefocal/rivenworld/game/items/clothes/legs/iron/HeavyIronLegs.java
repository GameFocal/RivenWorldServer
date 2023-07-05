package com.gamefocal.rivenworld.game.items.clothes.legs.iron;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.HeavyIronLegs_R;

public class HeavyIronLegs extends PlayerLegItem {

    public HeavyIronLegs() {
        this.name = "Heavy Iron Pants";
        this.desc = "A pants made of iron, leather and cloth";
        this.icon = InventoryDataRow.Pants_Heavy_T6_Dark;
        this.mesh = InventoryDataRow.Pants_Heavy_T6_Dark;
        this.initDurability(165);
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavyIronLegs_R();
    }

    @Override
    public float defend() {
        return 35;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "HeavyIronLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
