package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchWallBlockRecipe;

public class ThatchWallBlockItem extends PlaceableInventoryItem<ThatchWallBlockItem> implements InventoryCraftingInterface {

    public ThatchWallBlockItem() {
        this.name = "Thatch Wall Block";
        this.desc = "A Wall made of Thatch";
        this.icon = InventoryDataRow.Thatch_Wall;
        this.mesh = InventoryDataRow.Thatch_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchWallBlockRecipe();
    }
}
