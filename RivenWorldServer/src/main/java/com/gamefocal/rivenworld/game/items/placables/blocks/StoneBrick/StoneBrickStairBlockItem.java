package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrickStairsBlockRecipe;

public class StoneBrickStairBlockItem extends PlaceableInventoryItem<StoneBrickStairBlockItem> implements InventoryCraftingInterface {

    public StoneBrickStairBlockItem() {
        this.name = "Stone Brick Stairs";
        this.desc = "A stair of stone bricks";
        this.icon = InventoryDataRow.StoneBrickStairs_Block;
        this.mesh = InventoryDataRow.StoneBrickStairs_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickStairsBlockRecipe();
    }
}
