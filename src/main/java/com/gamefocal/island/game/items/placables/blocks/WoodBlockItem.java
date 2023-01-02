package com.gamefocal.island.game.items.placables.blocks;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.WoodBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.InventoryCraftingInterface;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.island.game.recipes.WoodBlockRecipe;

public class WoodBlockItem extends PlaceableInventoryItem<WoodBlockItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "Wood_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBlockRecipe();
    }
}
