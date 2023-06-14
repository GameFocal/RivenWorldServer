package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class PlasterRoundCornerBlockItem extends PlaceableInventoryItem<PlasterRoundCornerBlockItem> implements InventoryCraftingInterface {

    public PlasterRoundCornerBlockItem() {
        this.name = "Plaster Round corner Block";
        this.desc = "A block made of Plaster";
        this.icon = InventoryDataRow.Plaster_RoundCorner;
        this.mesh = InventoryDataRow.Plaster_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plasterroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
