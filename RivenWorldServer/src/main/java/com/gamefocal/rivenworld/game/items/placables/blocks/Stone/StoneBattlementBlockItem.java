package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.StoneBattlementBlockRecipe;

public class StoneBattlementBlockItem extends PlaceableInventoryItem<StoneBattlementBlockItem> implements InventoryCraftingInterface {

    public StoneBattlementBlockItem() {
        this.name = "Stone Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.StoneBattlement_Block;
        this.mesh = InventoryDataRow.StoneBattlement_Block;
        this.placable.IsBuildingBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBattlementBlockRecipe();
    }
}
