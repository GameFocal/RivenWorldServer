package com.gamefocal.rivenworld.game.items.resources.animals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.resources.CookedMeatRecipe;

public class CookedRedMeat extends ConsumableInventoryItem implements InventoryCraftingInterface {
    public CookedRedMeat() {
        this.icon = InventoryDataRow.Cooked_Meat;
        this.mesh = InventoryDataRow.Cooked_Meat;
        this.name = "Cooked Meat";
        this.desc = "Meat cooked on a fire";
        this.useThirstModifier = true;
        this.thirstModifier = -5;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        if (connection != null) {
            connection.getPlayer().playerStats.health += 10;
        }
        return 25;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CookedMeatRecipe();
    }
}
