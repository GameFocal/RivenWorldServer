package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.Dirt1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.Glass1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class Glass1_4CircleBlockItem extends PlaceableInventoryItem<Glass1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Glass1_4CircleBlockItem() {
        this.name = "Glass 1_4 Circle Block";
        this.desc = "A block made of Glass";
        this.icon = InventoryDataRow.Glass_RoundCorner2;
        this.mesh = InventoryDataRow.Glass_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glass1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Glass1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
