package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtHalfBlockItem extends PlaceableInventoryItem<DirtHalfBlockItem> implements InventoryCraftingInterface {

    public DirtHalfBlockItem() {
        this.name = "Dirt Half Block";
        this.desc = "A half block made of Dirt";
        this.icon = InventoryDataRow.Dirt_HalfBlock;
        this.mesh = InventoryDataRow.Dirt_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("dirthalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
