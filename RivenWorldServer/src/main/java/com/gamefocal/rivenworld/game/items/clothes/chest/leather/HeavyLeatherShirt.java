package com.gamefocal.rivenworld.game.items.clothes.chest.leather;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.HeavyLeatherShirt_R;

public class HeavyLeatherShirt extends PlayerChestItem {

    public HeavyLeatherShirt() {
        this.name = "Heavy Leather Shirt";
        this.desc = "A shirt made of leather and cloth";
        this.icon = InventoryDataRow.Chest1_Medium_T4;
        this.mesh = InventoryDataRow.Chest1_Medium_T4;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new HeavyLeatherShirt_R();
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
        return "HeavyLeatherShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
