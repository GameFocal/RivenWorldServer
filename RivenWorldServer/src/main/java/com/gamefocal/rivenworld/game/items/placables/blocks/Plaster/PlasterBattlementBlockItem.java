package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class PlasterBattlementBlockItem extends PlaceableInventoryItem<PlasterBattlementBlockItem> implements InventoryCraftingInterface {

    public PlasterBattlementBlockItem() {
        this.name = "Plaster Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.PlasterBattlement;
        this.mesh = InventoryDataRow.PlasterBattlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plastercastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
