package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldWallBlockRecipe;

public class GoldWallBlockItem extends PlaceableInventoryItem<GoldWallBlockItem> implements InventoryCraftingInterface {

    public GoldWallBlockItem() {
        this.name = "Gold Wall Block";
        this.desc = "A Wall made of Gold";
        this.icon = InventoryDataRow.Gold_Wall;
        this.mesh = InventoryDataRow.Gold_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldWallBlockRecipe();
    }
}
