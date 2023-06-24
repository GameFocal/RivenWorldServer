package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogTileBlockRecipe;

public class LogTileBlockItem extends PlaceableInventoryItem<LogTileBlockItem> implements InventoryCraftingInterface {

    public LogTileBlockItem() {
        this.name = "Log Tile Block";
        this.desc = "A Tile made of Log";
        this.icon = InventoryDataRow.Log_Tile;
        this.mesh = InventoryDataRow.Log_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logtileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogTileBlockRecipe();
    }
}
