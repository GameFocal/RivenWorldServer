package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronWallBlockRecipe;

public class IronWallBlockItem extends PlaceableInventoryItem<IronWallBlockItem> implements InventoryCraftingInterface {

    public IronWallBlockItem() {
        this.name = "Iron Wall Block";
        this.desc = "A Wall made of Iron";
        this.icon = InventoryDataRow.Iron_Wall;
        this.mesh = InventoryDataRow.Iron_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronWallBlockRecipe();
    }
}
