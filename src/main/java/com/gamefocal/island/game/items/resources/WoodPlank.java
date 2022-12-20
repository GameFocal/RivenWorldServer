package com.gamefocal.island.game.items.resources;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryCraftingInterface;
import com.gamefocal.island.game.recipes.PlankRecipe;

import java.util.LinkedList;

public class WoodPlank extends InventoryItem implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "Wooden_Plank";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlankRecipe();
    }
}
