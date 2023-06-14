package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtRampBlockItem extends PlaceableInventoryItem<DirtRampBlockItem> implements InventoryCraftingInterface {

    public DirtRampBlockItem() {
        this.name = "Dirt Ramp";
        this.desc = "A ramp made of Dirt";
        this.icon = InventoryDataRow.Dirt_Ramp;
        this.mesh = InventoryDataRow.Dirt_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
