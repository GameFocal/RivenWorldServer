package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronRoundCornerBlockRecipe;

public class IronRoundCornerBlockItem extends PlaceableInventoryItem<IronRoundCornerBlockItem> implements InventoryCraftingInterface {

    public IronRoundCornerBlockItem() {
        this.name = "Iron Round corner Block";
        this.desc = "A block made of Iron";
        this.icon = InventoryDataRow.Iron_RoundCorner;
        this.mesh = InventoryDataRow.Iron_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronRoundCornerBlockRecipe();
    }
}
