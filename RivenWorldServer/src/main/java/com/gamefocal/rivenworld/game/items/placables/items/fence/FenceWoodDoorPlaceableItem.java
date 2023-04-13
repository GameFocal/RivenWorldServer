package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.FenceLogDoorPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.fence.FenceWoodDoorPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FenceLogDoorPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FenceWoodDoorPlaceableRecipe;

public class FenceWoodDoorPlaceableItem extends PlaceableInventoryItem<FenceWoodDoorPlaceableItem> implements InventoryCraftingInterface {

    public FenceWoodDoorPlaceableItem() {
        this.name = "Tall Wood Fence Door";
        this.desc = "A tall door made out of wood.";
        this.icon = InventoryDataRow.Fence_woodDoor;
        this.mesh = InventoryDataRow.Fence_woodDoor;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new FenceWoodDoorPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FenceWoodDoorPlaceableRecipe();
    }
}
