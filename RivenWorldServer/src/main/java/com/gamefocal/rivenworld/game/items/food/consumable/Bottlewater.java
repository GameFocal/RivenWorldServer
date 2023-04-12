package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Bottlewater extends ConsumableInventoryItem implements InventoryCraftingInterface {

    public Bottlewater() {
        this.icon = InventoryDataRow.Bottlewater;
        this.mesh = InventoryDataRow.Bottlewater;
        this.name = "Bottlewater";
        this.desc = ""; // TODO: ZP;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return null;
    }
}
