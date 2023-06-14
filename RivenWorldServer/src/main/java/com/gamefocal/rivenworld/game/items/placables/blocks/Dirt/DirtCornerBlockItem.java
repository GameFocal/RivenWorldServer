package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtCornerBlockItem extends PlaceableInventoryItem<DirtCornerBlockItem> implements InventoryCraftingInterface {

    public DirtCornerBlockItem() {
        this.name = "Dirt Corner Block";
        this.desc = "A corner block made of Dirt";
        this.icon = InventoryDataRow.Dirt_CornerBlock;
        this.mesh = InventoryDataRow.Dirt_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
