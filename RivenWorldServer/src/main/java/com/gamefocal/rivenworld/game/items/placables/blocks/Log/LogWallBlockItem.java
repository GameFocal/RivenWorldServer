package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogWallBlockRecipe;

public class LogWallBlockItem extends PlaceableInventoryItem<LogWallBlockItem> implements InventoryCraftingInterface {

    public LogWallBlockItem() {
        this.name = "Log Wall Block";
        this.desc = "A Wall made of Log";
        this.icon = InventoryDataRow.Log_Wall;
        this.mesh = InventoryDataRow.Log_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogWallBlockRecipe();
    }
}
