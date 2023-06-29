package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.Copper1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.Dirt1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Dirt.Dirt1_4CircleBlockRecipe;

public class Dirt1_4CircleBlockItem extends PlaceableInventoryItem<Dirt1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Dirt1_4CircleBlockItem() {
        this.name = "Dirt 1_4 Circle Block";
        this.desc = "A block made of dirt";
        this.icon = InventoryDataRow.Dirt_RoundCorner2;
        this.mesh = InventoryDataRow.Dirt_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirt1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Dirt1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Dirt1_4CircleBlockRecipe();
    }
}
