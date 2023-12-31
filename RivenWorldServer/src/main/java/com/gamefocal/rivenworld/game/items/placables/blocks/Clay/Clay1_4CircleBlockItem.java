package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.Clay1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.Wood1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.Clay1_4CircleBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.Wood1_4CircleBlockRecipe;

public class Clay1_4CircleBlockItem extends PlaceableInventoryItem<Clay1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Clay1_4CircleBlockItem() {
        this.name = "Clay 1_4 Circle Block";
        this.desc = "A block made of Clay";
        this.icon = InventoryDataRow.Clay_RoundCorner2;
        this.mesh = InventoryDataRow.Clay_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("clay1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Clay1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Clay1_4CircleBlockRecipe();
    }
}
