package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.Clay1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.Copper1_4CircleBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.Copper1_4CircleBlockRecipe;

public class Copper1_4CircleBlockItem extends PlaceableInventoryItem<Copper1_4CircleBlockItem> implements InventoryCraftingInterface {

    public Copper1_4CircleBlockItem() {
        this.name = "Copper 1_4 Circle Block";
        this.desc = "A block made of Copper";
        this.icon = InventoryDataRow.Copper_RoundCorner2;
        this.mesh = InventoryDataRow.Copper_RoundCorner2;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("copper1_4circleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new Copper1_4CircleBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Copper1_4CircleBlockRecipe();
    }
}
