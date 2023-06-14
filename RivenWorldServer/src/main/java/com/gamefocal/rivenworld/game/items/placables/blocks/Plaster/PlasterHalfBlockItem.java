package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class PlasterHalfBlockItem extends PlaceableInventoryItem<PlasterHalfBlockItem> implements InventoryCraftingInterface {

    public PlasterHalfBlockItem() {
        this.name = "Plaster Half Block";
        this.desc = "A half block made of Plaster";
        this.icon = InventoryDataRow.Plaster_HalfBlock;
        this.mesh = InventoryDataRow.Plaster_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("plasterhalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
