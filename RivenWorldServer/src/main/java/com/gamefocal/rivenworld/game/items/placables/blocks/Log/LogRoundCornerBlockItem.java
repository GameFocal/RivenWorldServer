package com.gamefocal.rivenworld.game.items.placables.blocks.Log;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogHalfBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogRoundCornerBlockRecipe;

public class LogRoundCornerBlockItem extends PlaceableInventoryItem<LogRoundCornerBlockItem> implements InventoryCraftingInterface {

    public LogRoundCornerBlockItem() {
        this.name = "Log Round corner Block";
        this.desc = "A block made of Log";
        this.icon = InventoryDataRow.Log_RoundCorner;
        this.mesh = InventoryDataRow.Log_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("logroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new LogRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LogRoundCornerBlockRecipe();
    }
}
