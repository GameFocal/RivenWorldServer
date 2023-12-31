package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchBlockRecipe;

public class ThatchBlockItem extends PlaceableInventoryItem<ThatchBlockItem> implements InventoryCraftingInterface {

    public ThatchBlockItem() {
        this.name = "Thatch Block";
        this.desc = "A block made of thatch";
        this.icon = InventoryDataRow.Thatch_Block;
        this.mesh = InventoryDataRow.Thatch_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchBlockRecipe();
    }
}
