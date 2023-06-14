package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.Gold1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.Iron1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class Iron1_4CircleBlockItem extends PlaceableInventoryItem<Iron1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Iron1_4CircleBlockItem() {
        this.name = "Iron 1_4 Circle Block";
        this.desc = "A block made of Iron";
        this.icon = InventoryDataRow.Iron_RoundCorner2;
        this.mesh = InventoryDataRow.Iron_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("iron1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Iron1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
