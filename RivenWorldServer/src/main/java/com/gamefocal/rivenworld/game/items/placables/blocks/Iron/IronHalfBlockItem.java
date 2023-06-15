package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronHalfBlockRecipe;

public class IronHalfBlockItem extends PlaceableInventoryItem<IronHalfBlockItem> implements InventoryCraftingInterface {

    public IronHalfBlockItem() {
        this.name = "Iron Half Block";
        this.desc = "A half block made of Iron";
        this.icon = InventoryDataRow.Iron_HalfBlock;
        this.mesh = InventoryDataRow.Iron_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("ironhalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronHalfBlockRecipe();
    }
}
