package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodCornerBlockRecipe;

public class ClayCornerBlockItem extends PlaceableInventoryItem<ClayCornerBlockItem> implements InventoryCraftingInterface {

    public ClayCornerBlockItem() {
        this.name = "Clay Corner Block";
        this.desc = "A corner block made of Clay";
        this.icon = InventoryDataRow.Clay_CornerBlock;
        this.mesh = InventoryDataRow.Clay_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claycorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
