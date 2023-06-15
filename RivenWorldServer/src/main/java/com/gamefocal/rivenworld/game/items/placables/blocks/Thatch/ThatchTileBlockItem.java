package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchTileBlockRecipe;

public class ThatchTileBlockItem extends PlaceableInventoryItem<ThatchTileBlockItem> implements InventoryCraftingInterface {

    public ThatchTileBlockItem() {
        this.name = "Thatch Tile Block";
        this.desc = "A Tile made of Thatch";
        this.icon = InventoryDataRow.Thatch_Tile;
        this.mesh = InventoryDataRow.Thatch_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchtileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchTileBlockRecipe();
    }
}
