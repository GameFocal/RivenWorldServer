package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Dirt.DirtTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Dirt.DirtWallBlockRecipe;

public class DirtWallBlockItem extends PlaceableInventoryItem<DirtWallBlockItem> implements InventoryCraftingInterface {

    public DirtWallBlockItem() {
        this.name = "Dirt Wall Block";
        this.desc = "A Wall made of Dirt";
        this.icon = InventoryDataRow.Dirt_Wall;
        this.mesh = InventoryDataRow.Dirt_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtWallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DirtWallBlockRecipe();
    }
}
