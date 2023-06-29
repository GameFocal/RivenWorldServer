package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchBattlementCornerBlockRecipe;

public class ThatchBattlementCornerBlockItem extends PlaceableInventoryItem<ThatchBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public ThatchBattlementCornerBlockItem() {
        this.name = "Thatch Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Thatch_BattlementCorner;
        this.mesh = InventoryDataRow.Thatch_BattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchBattlementCornerBlockRecipe();
    }
}
