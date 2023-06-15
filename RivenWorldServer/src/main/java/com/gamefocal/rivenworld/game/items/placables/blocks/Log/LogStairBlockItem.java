package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogStairsBlockRecipe;

public class LogStairBlockItem extends PlaceableInventoryItem<LogStairBlockItem> implements InventoryCraftingInterface {

    public LogStairBlockItem() {
        this.name = "Log Stairs";
        this.desc = "A stair made of Log";
        this.icon = InventoryDataRow.Log_Stairs;
        this.mesh = InventoryDataRow.Log_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogStairsBlockRecipe();
    }
}
