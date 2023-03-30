package com.gamefocal.rivenworld.game.items.clothes.feet.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerShoesItem;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SimpleLeatherShoes_R;

public class SimpleLeatherShoes extends PlayerShoesItem {

    public SimpleLeatherShoes() {
        this.name = "Leather Shoes";
        this.desc = "Shoes made form leather and fiber";
        this.icon = InventoryDataRow.Boots0_Light_T2;
        this.mesh = InventoryDataRow.Boots0_Light_T2;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleLeatherShoes_R();
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
        return null;
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
