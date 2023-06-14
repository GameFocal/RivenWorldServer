package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class PlasterBattlementCornerBlockItem extends PlaceableInventoryItem<PlasterBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public PlasterBattlementCornerBlockItem() {
        this.name = "Plaster Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.PlasterBattlementCorner;
        this.mesh = InventoryDataRow.PlasterBattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plastercastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
