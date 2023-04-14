package com.gamefocal.rivenworld.game.items.resources.wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.recipes.PlankRecipe;

public class WoodPlank extends InventoryItem implements InventoryCraftingInterface {

    public WoodPlank() {
        this.icon = InventoryDataRow.Wooden_Plank;
        this.mesh = InventoryDataRow.Wooden_Plank;
        this.name = "Wooden Plank";
        this.desc = "Made from splitting a log into parts";
        this.spawnNames.add("plank");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlankRecipe();
    }
}
