package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class IronTileBlockItem extends PlaceableInventoryItem<IronTileBlockItem> implements InventoryCraftingInterface {

    public IronTileBlockItem() {
        this.name = "Iron Tile Block";
        this.desc = "A Tile made of Iron";
        this.icon = InventoryDataRow.Iron_Tile;
        this.mesh = InventoryDataRow.Iron_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("irontileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
