package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class LogBattlementCornerBlockItem extends PlaceableInventoryItem<LogBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public LogBattlementCornerBlockItem() {
        this.name = "Log Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Log_BattlementCorner;
        this.mesh = InventoryDataRow.Log_BattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
