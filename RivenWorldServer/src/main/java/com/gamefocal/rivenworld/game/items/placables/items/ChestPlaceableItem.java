package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.storage.ChestPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.ChestPlaceableRecipe;

public class ChestPlaceableItem extends PlaceableInventoryItem<ChestPlaceableItem> implements InventoryCraftingInterface {

    public ChestPlaceableItem() {
        this.name = "Chest";
        this.desc = "A box to put your stuff into";
        this.icon = InventoryDataRow.ChestPlaceable;
        this.mesh = InventoryDataRow.ChestPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("chest");
    }

    @Override
    public GameEntity spawnItem() {
        return new ChestPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChestPlaceableRecipe();
    }
}
