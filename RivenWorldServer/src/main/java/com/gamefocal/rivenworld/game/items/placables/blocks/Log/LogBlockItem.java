package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogBlockRecipe;

public class LogBlockItem extends PlaceableInventoryItem<LogBlockItem> implements InventoryCraftingInterface {

    public LogBlockItem() {
        this.name = "Log Block";
        this.desc = "A block of logs";
        this.icon = InventoryDataRow.Log_Block;
        this.mesh = InventoryDataRow.Log_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogBlockRecipe();
    }
}
