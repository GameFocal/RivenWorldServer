package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickCornerBlockRecipe;

public class StoneBrickCornerBlockItem extends PlaceableInventoryItem<StoneBrickCornerBlockItem> implements InventoryCraftingInterface {

    public StoneBrickCornerBlockItem() {
        this.name = "Stone Brick Corner Block";
        this.desc = "A corner block made of stone bricks";
        this.icon = InventoryDataRow.StoneBrickCorner_Block;
        this.mesh = InventoryDataRow.StoneBrickCorner_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickCornerBlockRecipe();
    }
}
