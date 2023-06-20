package com.gamefocal.rivenworld.game.items.resources.wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.recipes.weapons.StickRecipe;

public class WoodStick extends InventoryItem implements InventoryCraftingInterface {

    public WoodStick() {
        this.icon = InventoryDataRow.Stick;
        this.mesh = InventoryDataRow.Stick;
        this.name = "Stick";
        this.desc = "Found in trees and on the forest ground";
        this.spawnNames.add("stick");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StickRecipe();
    }
}
