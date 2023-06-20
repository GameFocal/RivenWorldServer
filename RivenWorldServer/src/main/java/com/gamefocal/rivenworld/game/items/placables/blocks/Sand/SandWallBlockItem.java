package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandWallBlockRecipe;

public class SandWallBlockItem extends PlaceableInventoryItem<SandWallBlockItem> implements InventoryCraftingInterface {

    public SandWallBlockItem() {
        this.name = "Sand Wall Block";
        this.desc = "A Wall made of Sand";
        this.icon = InventoryDataRow.Sand_Wall;
        this.mesh = InventoryDataRow.Sand_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SandWallBlockRecipe();
    }
}
