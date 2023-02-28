package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.lights.StandOilLampPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.StandOilLampPlaceableRecipe;

public class StandOilLampPlaceableItem extends PlaceableInventoryItem<StandOilLampPlaceableItem> implements InventoryCraftingInterface {

    public StandOilLampPlaceableItem() {
        this.icon = InventoryDataRow.StandOilLampPlaceable;
        this.mesh = InventoryDataRow.StandOilLampPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new StandOilLampPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StandOilLampPlaceableRecipe();
    }
}
