package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrickBattlementBlockRecipe;

public class StoneBrickBattlementBlockItem extends PlaceableInventoryItem<StoneBrickBattlementBlockItem> implements InventoryCraftingInterface {

    public StoneBrickBattlementBlockItem() {
        this.name = "Stone Brick Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.StoneBrickBattlement_Block;
        this.mesh = InventoryDataRow.StoneBrickBattlement_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickBattlementBlockRecipe();
    }
}
