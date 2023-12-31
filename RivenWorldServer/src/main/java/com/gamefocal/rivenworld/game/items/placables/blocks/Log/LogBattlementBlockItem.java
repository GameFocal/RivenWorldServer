package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogBattlementBlockRecipe;

public class LogBattlementBlockItem extends PlaceableInventoryItem<LogBattlementBlockItem> implements InventoryCraftingInterface {

    public LogBattlementBlockItem() {
        this.name = "Log Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Log_Battlement;
        this.mesh = InventoryDataRow.Log_Battlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogBattlementBlockRecipe();
    }
}
