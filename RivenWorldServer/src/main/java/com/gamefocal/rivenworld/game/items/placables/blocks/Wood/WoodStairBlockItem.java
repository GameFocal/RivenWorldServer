package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.WoodStairsBlockRecipe;

public class WoodStairBlockItem extends PlaceableInventoryItem<WoodStairBlockItem> implements InventoryCraftingInterface {

    public WoodStairBlockItem() {
        this.name = "Wooden Stairs";
        this.desc = "A stair of wood";
        this.icon = InventoryDataRow.WoodStairs_Block;
        this.mesh = InventoryDataRow.WoodStairs_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodStairsBlockRecipe();
    }
}
