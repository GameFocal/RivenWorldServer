package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronBlockRecipe;

public class IronBlockItem extends PlaceableInventoryItem<IronBlockItem> implements InventoryCraftingInterface {

    public IronBlockItem() {
        this.name = "Iron Block";
        this.desc = "A block of iron";
        this.icon = InventoryDataRow.Iron_Block;
        this.mesh = InventoryDataRow.Iron_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronBlockRecipe();
    }
}
