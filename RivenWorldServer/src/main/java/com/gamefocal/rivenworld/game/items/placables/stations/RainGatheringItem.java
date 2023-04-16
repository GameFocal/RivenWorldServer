package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.PassiveRainWaterStation;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.RainGatheringRecipe;

public class RainGatheringItem extends PlaceableInventoryItem<SpeeningWheelItem> implements InventoryCraftingInterface {

    public RainGatheringItem() {
        this.name = "Rain Collector";
        this.desc = "Collects passive rain water for you";
        this.spawnNames.add("rainwell");
        this.icon = InventoryDataRow.water_r;
        this.mesh = InventoryDataRow.water_r;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new PassiveRainWaterStation();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new RainGatheringRecipe();
    }
}
