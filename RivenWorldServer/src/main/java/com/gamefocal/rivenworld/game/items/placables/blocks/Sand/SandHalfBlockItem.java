package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class SandHalfBlockItem extends PlaceableInventoryItem<SandHalfBlockItem> implements InventoryCraftingInterface {

    public SandHalfBlockItem() {
        this.name = "Sand Half Block";
        this.desc = "A half block made of Sand";
        this.icon = InventoryDataRow.Sand_HalfBlock;
        this.mesh = InventoryDataRow.Sand_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("sandhalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
