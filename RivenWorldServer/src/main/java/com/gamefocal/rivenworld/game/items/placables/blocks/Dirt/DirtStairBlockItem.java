package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtStairBlockItem extends PlaceableInventoryItem<DirtStairBlockItem> implements InventoryCraftingInterface {

    public DirtStairBlockItem() {
        this.name = "Dirt Stairs";
        this.desc = "A stair made of Dirt";
        this.icon = InventoryDataRow.Dirt_Stairs;
        this.mesh = InventoryDataRow.Dirt_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
