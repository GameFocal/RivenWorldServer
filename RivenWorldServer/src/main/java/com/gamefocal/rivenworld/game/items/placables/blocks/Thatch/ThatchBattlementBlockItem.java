package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class ThatchBattlementBlockItem extends PlaceableInventoryItem<ThatchBattlementBlockItem> implements InventoryCraftingInterface {

    public ThatchBattlementBlockItem() {
        this.name = "Thatch Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Thatch_Battlement;
        this.mesh = InventoryDataRow.Thatch_Battlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
