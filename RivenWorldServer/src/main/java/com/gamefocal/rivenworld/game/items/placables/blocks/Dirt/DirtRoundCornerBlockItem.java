package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtRoundCornerBlockItem extends PlaceableInventoryItem<DirtRoundCornerBlockItem> implements InventoryCraftingInterface {

    public DirtRoundCornerBlockItem() {
        this.name = "Dirt Round corner Block";
        this.desc = "A block made of Dirt";
        this.icon = InventoryDataRow.Dirt_RoundCorner;
        this.mesh = InventoryDataRow.Dirt_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
