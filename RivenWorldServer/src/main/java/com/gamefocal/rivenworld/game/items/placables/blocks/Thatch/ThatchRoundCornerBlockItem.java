package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchRoundCornerBlockRecipe;

public class ThatchRoundCornerBlockItem extends PlaceableInventoryItem<ThatchRoundCornerBlockItem> implements InventoryCraftingInterface {

    public ThatchRoundCornerBlockItem() {
        this.name = "Thatch Round corner Block";
        this.desc = "A block made of Thatch";
        this.icon = InventoryDataRow.Thatch_RoundCorner;
        this.mesh = InventoryDataRow.Thatch_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchRoundCornerBlockRecipe();
    }
}
