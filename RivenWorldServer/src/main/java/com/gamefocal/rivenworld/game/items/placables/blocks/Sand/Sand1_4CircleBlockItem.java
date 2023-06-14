package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.Log1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.Sand1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class Sand1_4CircleBlockItem extends PlaceableInventoryItem<Sand1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Sand1_4CircleBlockItem() {
        this.name = "Sand 1_4 Circle Block";
        this.desc = "A block made of Sand";
        this.icon = InventoryDataRow.Sand_RoundCorner2;
        this.mesh = InventoryDataRow.Sand_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sand1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Sand1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
