package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class LogCornerBlockItem extends PlaceableInventoryItem<LogCornerBlockItem> implements InventoryCraftingInterface {

    public LogCornerBlockItem() {
        this.name = "Log Corner Block";
        this.desc = "A corner block made of Log";
        this.icon = InventoryDataRow.Log_CornerBlock;
        this.mesh = InventoryDataRow.Log_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
