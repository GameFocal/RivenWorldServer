package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.Plaster1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class Plaster1_4CircleBlockItem extends PlaceableInventoryItem<Plaster1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Plaster1_4CircleBlockItem() {
        this.name = "Plaster 1_4 Circle Block";
        this.desc = "A block made of Plaster";
        this.icon = InventoryDataRow.Plaster_RoundCorner2;
        this.mesh = InventoryDataRow.Plaster_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plaster1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Plaster1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
