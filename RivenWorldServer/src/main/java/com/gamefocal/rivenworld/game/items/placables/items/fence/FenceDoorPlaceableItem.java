package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.FenceDoorPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FenceDoorPlaceableRecipe;

public class FenceDoorPlaceableItem extends PlaceableInventoryItem<FenceDoorPlaceableItem> implements InventoryCraftingInterface {

    public FenceDoorPlaceableItem() {
        this.name = "Wooden Fence Door";
        this.desc = "A Door for your fence.";
        this.icon = InventoryDataRow.FenceDoorPlaceable;
        this.mesh = InventoryDataRow.FenceDoorPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new FenceDoorPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FenceDoorPlaceableRecipe();
    }
}
