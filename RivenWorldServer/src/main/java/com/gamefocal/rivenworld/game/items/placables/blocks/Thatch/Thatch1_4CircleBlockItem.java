package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.Log1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.Thatch1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.Thatch1_4CircleBlockRecipe;

public class Thatch1_4CircleBlockItem extends PlaceableInventoryItem<Thatch1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Thatch1_4CircleBlockItem() {
        this.name = "Thatch 1_4 Circle Block";
        this.desc = "A block made of Thatch";
        this.icon = InventoryDataRow.Thatch_RoundCorner2;
        this.mesh = InventoryDataRow.Thatch_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatch1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Thatch1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Thatch1_4CircleBlockRecipe();
    }
}
