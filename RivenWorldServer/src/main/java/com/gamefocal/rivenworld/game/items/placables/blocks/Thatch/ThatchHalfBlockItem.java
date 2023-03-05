package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.ThatchHalfBlockRecipe;

public class ThatchHalfBlockItem extends PlaceableInventoryItem<ThatchHalfBlockItem> implements InventoryCraftingInterface {

    public ThatchHalfBlockItem() {
        this.name = "Thatch Half Block";
        this.desc = "A half block made of thatch";
        this.icon = InventoryDataRow.ThatchHalf_Block;
        this.mesh = InventoryDataRow.ThatchHalf_Block;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchHalfBlockRecipe();
    }
}
