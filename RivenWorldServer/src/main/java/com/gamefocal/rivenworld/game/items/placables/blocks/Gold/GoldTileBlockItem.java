package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldTileBlockRecipe;

public class GoldTileBlockItem extends PlaceableInventoryItem<GoldTileBlockItem> implements InventoryCraftingInterface {

    public GoldTileBlockItem() {
        this.name = "Gold Tile Block";
        this.desc = "A Tile made of Gold";
        this.icon = InventoryDataRow.Gold_Tile;
        this.mesh = InventoryDataRow.Gold_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldtileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldTileBlockRecipe();
    }
}
