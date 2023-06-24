package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogRampBlockRecipe;

public class LogRampBlockItem extends PlaceableInventoryItem<LogRampBlockItem> implements InventoryCraftingInterface {

    public LogRampBlockItem() {
        this.name = "Log Ramp";
        this.desc = "A ramp made of Log";
        this.icon = InventoryDataRow.Log_Ramp;
        this.mesh = InventoryDataRow.Log_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogRampBlockRecipe();
    }
}
