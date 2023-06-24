package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayHalfBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperHalfBlockRecipe;

public class CopperHalfBlockItem extends PlaceableInventoryItem<CopperHalfBlockItem> implements InventoryCraftingInterface {

    public CopperHalfBlockItem() {
        this.name = "Copper Half Block";
        this.desc = "A half block made of Copper";
        this.icon = InventoryDataRow.Copper_HalfBlock;
        this.mesh = InventoryDataRow.Copper_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("copperhalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperHalfBlockRecipe();
    }
}
