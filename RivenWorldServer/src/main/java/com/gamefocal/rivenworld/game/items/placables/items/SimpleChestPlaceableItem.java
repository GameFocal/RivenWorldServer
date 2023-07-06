package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.storage.ChestPlaceable;
import com.gamefocal.rivenworld.game.entites.storage.SimpleChestPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.ChestPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.SimpleChestPlaceableRecipe;

public class SimpleChestPlaceableItem extends PlaceableInventoryItem<SimpleChestPlaceableItem> implements InventoryCraftingInterface {

    public SimpleChestPlaceableItem() {
        this.name = "Simple Chest";
        this.desc = "A box to put your stuff into";
        this.icon = InventoryDataRow.Wooden_Crate_04;
        this.mesh = InventoryDataRow.Wooden_Crate_04;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("simplechest");
    }

    @Override
    public GameEntity spawnItem() {
        return new SimpleChestPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SimpleChestPlaceableRecipe();
    }
}
