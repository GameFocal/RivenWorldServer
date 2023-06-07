package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Stone.StoneCornerBlockRecipe;

public class StoneCornerBlockItem extends PlaceableInventoryItem<StoneCornerBlockItem> implements InventoryCraftingInterface {

    public StoneCornerBlockItem() {
        this.name = "Stone Corner Block";
        this.desc = "A block made of stone";
        this.icon = InventoryDataRow.StoneCorner_Block;
        this.mesh = InventoryDataRow.StoneCorner_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneCornerBlockRecipe();
    }
}
