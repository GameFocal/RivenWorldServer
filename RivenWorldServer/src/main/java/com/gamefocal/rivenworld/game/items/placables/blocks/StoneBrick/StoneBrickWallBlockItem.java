package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickWallBlockRecipe;

public class StoneBrickWallBlockItem extends PlaceableInventoryItem<StoneBrickWallBlockItem> implements InventoryCraftingInterface {

    public StoneBrickWallBlockItem() {
        this.name = "StoneBrick Wall Block";
        this.desc = "A Wall made of StoneBrick";
        this.icon = InventoryDataRow.StoneBrick_Wall;
        this.mesh = InventoryDataRow.StoneBrick_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickWallBlockRecipe();
    }
}
