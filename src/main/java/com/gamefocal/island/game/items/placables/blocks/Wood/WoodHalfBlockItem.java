package com.gamefocal.island.game.items.placables.blocks.Wood;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.Wood.WoodBlock;
import com.gamefocal.island.game.entites.blocks.Wood.WoodHalfBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.InventoryCraftingInterface;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.island.game.recipes.WoodBlockRecipe;

public class WoodHalfBlockItem extends PlaceableInventoryItem<WoodHalfBlockItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "WoodHalf_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBlockRecipe();
    }
}
