package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.StoneHalfBlockRecipe;

public class StoneHalfBlockItem extends PlaceableInventoryItem<StoneHalfBlockItem> implements InventoryCraftingInterface {

    public StoneHalfBlockItem() {
        this.icon = InventoryDataRow.StoneHalf_Block;
        this.mesh = InventoryDataRow.StoneHalf_Block;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneHalfBlockRecipe();
    }
}
