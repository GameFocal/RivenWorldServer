package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class IronCornerBlockItem extends PlaceableInventoryItem<IronCornerBlockItem> implements InventoryCraftingInterface {

    public IronCornerBlockItem() {
        this.name = "Iron Corner Block";
        this.desc = "A corner block made of Iron";
        this.icon = InventoryDataRow.IronCornerBlock;
        this.mesh = InventoryDataRow.IronCornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
