package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperCornerBlockRecipe;

public class CopperCornerBlockItem extends PlaceableInventoryItem<CopperCornerBlockItem> implements InventoryCraftingInterface {

    public CopperCornerBlockItem() {
        this.name = "Copper Corner Block";
        this.desc = "A corner block made of Copper";
        this.icon = InventoryDataRow.Copper_CornerBlock;
        this.mesh = InventoryDataRow.Copper_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("coppercorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperCornerBlockRecipe();
    }
}
