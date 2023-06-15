package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.Stone1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrick1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrick1_4CircleBlockRecipe;

public class StoneBrick1_4CircleBlockItem extends PlaceableInventoryItem<StoneBrick1_4CircleBlockItem> implements InventoryCraftingInterface {

    public StoneBrick1_4CircleBlockItem() {
        this.name = "StoneBrick 1_4 Circle Block";
        this.desc = "A block made of StoneBrick";
        this.icon = InventoryDataRow.StoneBrick_RoundCorner2;
        this.mesh = InventoryDataRow.StoneBrick_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrick1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrick1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrick1_4CircleBlockRecipe();
    }
}
