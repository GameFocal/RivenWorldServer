package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.FenceDoorPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.fence.FenceLogDoorPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FenceDoorPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FenceLogDoorPlaceableRecipe;

public class FenceLogDoorPlaceableItem extends PlaceableInventoryItem<FenceLogDoorPlaceableItem> implements InventoryCraftingInterface {

    public FenceLogDoorPlaceableItem() {
        this.name = "Fence log Door";
        this.desc = "A Door made out of logs.";
        this.icon = InventoryDataRow.Fence_logDoor;
        this.mesh = InventoryDataRow.Fence_logDoor;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("logdoorfence");
    }

    @Override
    public GameEntity spawnItem() {
        return new FenceLogDoorPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FenceLogDoorPlaceableRecipe();
    }
}
