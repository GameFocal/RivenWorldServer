package com.gamefocal.rivenworld.game.items.clothes.head;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerHeadItem;
import com.gamefocal.rivenworld.game.recipes.clothing.head.ClothCap_R;

public class CloakHead extends PlayerHeadItem {

    public CloakHead() {
        this.name = "Cloth Cloak";
        this.desc = "A piece of cloth headware";
        this.icon = InventoryDataRow.Helm0_Hunt_T2;
        this.mesh = InventoryDataRow.Helm0_Hunt_T2;
    }

    @Override
    public float defend() {
        return 1;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClothCap_R();
    }
}
