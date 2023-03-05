package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.ThatchStairsBlockRecipe;

public class ThatchStairBlockItem extends PlaceableInventoryItem<ThatchStairBlockItem> implements InventoryCraftingInterface {

    public ThatchStairBlockItem() {
        this.name = "Thatch Stairs";
        this.desc = "A stair of thatch";
        this.icon = InventoryDataRow.ThatchStairs_Block;
        this.mesh = InventoryDataRow.ThatchStairs_Block;
        this.placable.IsBuildingBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchStairsBlockRecipe();
    }
}
