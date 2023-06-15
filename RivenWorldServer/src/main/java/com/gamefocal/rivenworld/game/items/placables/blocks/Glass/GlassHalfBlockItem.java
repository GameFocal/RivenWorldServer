package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassHalfBlockRecipe;

public class GlassHalfBlockItem extends PlaceableInventoryItem<GlassHalfBlockItem> implements InventoryCraftingInterface {

    public GlassHalfBlockItem() {
        this.name = "Glass Half Block";
        this.desc = "A half block made of Glass";
        this.icon = InventoryDataRow.Glass_HalfBlock;
        this.mesh = InventoryDataRow.Glass_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("glasshalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassHalfBlockRecipe();
    }
}
