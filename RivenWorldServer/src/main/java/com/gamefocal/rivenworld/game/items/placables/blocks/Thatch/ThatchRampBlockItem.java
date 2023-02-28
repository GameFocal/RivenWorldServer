package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.ThatchRampBlockRecipe;

public class ThatchRampBlockItem extends PlaceableInventoryItem<ThatchRampBlockItem> implements InventoryCraftingInterface {

    public ThatchRampBlockItem() {
        this.icon = InventoryDataRow.ThatchRamp_Block;
        this.mesh = InventoryDataRow.ThatchRamp_Block;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchRampBlockRecipe();
    }
}
