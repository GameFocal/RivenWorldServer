package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperRoundCornerBlockRecipe;

public class CopperRoundCornerBlockItem extends PlaceableInventoryItem<CopperRoundCornerBlockItem> implements InventoryCraftingInterface {

    public CopperRoundCornerBlockItem() {
        this.name = "Copper Round corner Block";
        this.desc = "A block made of Copper";
        this.icon = InventoryDataRow.Copper_RoundCorner;
        this.mesh = InventoryDataRow.Copper_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("copperroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperRoundCornerBlockRecipe();
    }
}
