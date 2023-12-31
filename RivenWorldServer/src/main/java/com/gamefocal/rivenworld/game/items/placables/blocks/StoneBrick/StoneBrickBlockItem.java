package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickBlockRecipe;

public class StoneBrickBlockItem extends PlaceableInventoryItem<StoneBrickBlockItem> implements InventoryCraftingInterface {

    public StoneBrickBlockItem() {
        this.name = "Stone Brick Block";
        this.desc = "A block made of stone bricks";
        this.icon = InventoryDataRow.StoneBrick_Block;
        this.mesh = InventoryDataRow.StoneBrick_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickBlockRecipe();
    }
}
