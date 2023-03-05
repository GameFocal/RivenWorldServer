package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.StoneBrickHalfBlockRecipe;

public class StoneBrickHalfBlockItem extends PlaceableInventoryItem<StoneBrickHalfBlockItem> implements InventoryCraftingInterface {

    public StoneBrickHalfBlockItem() {
        this.name = "Stone Brick Corner Block";
        this.desc = "A half block made of stone bricks";
        this.icon = InventoryDataRow.StoneBrickHalf_Block;
        this.mesh = InventoryDataRow.StoneBrickHalf_Block;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickHalfBlockRecipe();
    }
}
