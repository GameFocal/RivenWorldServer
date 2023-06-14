package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class StoneRoundCornerBlockItem extends PlaceableInventoryItem<StoneRoundCornerBlockItem> implements InventoryCraftingInterface {

    public StoneRoundCornerBlockItem() {
        this.name = "Stone Round corner Block";
        this.desc = "A block made of Stone";
        this.icon = InventoryDataRow.Stone_RoundCorner;
        this.mesh = InventoryDataRow.Stone_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stoneroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
