package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayRoundCornerBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodRoundCornerBlockRecipe;

public class ClayRoundCornerBlockItem extends PlaceableInventoryItem<ClayRoundCornerBlockItem> implements InventoryCraftingInterface {

    public ClayRoundCornerBlockItem() {
        this.name = "Clay Round corner Block";
        this.desc = "A block made of Clay";
        this.icon = InventoryDataRow.Clay_RoundCorner;
        this.mesh = InventoryDataRow.Clay_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("clayroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClayRoundCornerBlockRecipe();
    }
}
