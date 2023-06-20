package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Stone.StoneTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Stone.StoneWallBlockRecipe;

public class StoneWallBlockItem extends PlaceableInventoryItem<StoneWallBlockItem> implements InventoryCraftingInterface {

    public StoneWallBlockItem() {
        this.name = "Stone Wall Block";
        this.desc = "A Wall made of Stone";
        this.icon = InventoryDataRow.Stone_Wall;
        this.mesh = InventoryDataRow.Stone_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonewallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneWallBlockRecipe();
    }
}
