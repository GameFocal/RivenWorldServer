package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronRampBlockRecipe;

public class IronRampBlockItem extends PlaceableInventoryItem<IronRampBlockItem> implements InventoryCraftingInterface {

    public IronRampBlockItem() {
        this.name = "Iron Ramp";
        this.desc = "A ramp made of Iron";
        this.icon = InventoryDataRow.Iron_Ramp;
        this.mesh = InventoryDataRow.Iron_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronRampBlockRecipe();
    }
}
