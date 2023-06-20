package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBattlementCornerBlockRecipe;

public class StoneBattlementCornerBlockItem extends PlaceableInventoryItem<StoneBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public StoneBattlementCornerBlockItem() {
        this.name = "Stone Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.StoneBattlementCorner_Block;
        this.mesh = InventoryDataRow.StoneBattlementCorner_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonecastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBattlementCornerBlockRecipe();
    }
}
