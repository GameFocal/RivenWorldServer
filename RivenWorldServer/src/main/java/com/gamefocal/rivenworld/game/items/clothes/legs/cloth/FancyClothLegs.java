package com.gamefocal.rivenworld.game.items.clothes.legs.cloth;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerLegItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.FancyClothShirt_R;

public class FancyClothLegs extends PlayerLegItem {

    public FancyClothLegs() {
        this.name = "Fancy Cloth Pants";
        this.desc = "A nicer pants made of cloth";
        this.icon = InventoryDataRow.Pants_Light_T2;
        this.mesh = InventoryDataRow.Pants_Light_T2;
        this.durability = 150;
        this.maxDurability = 150;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FancyClothShirt_R();
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
        return "FancyClothLegs";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
