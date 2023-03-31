package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.MasonBench;
import com.gamefocal.rivenworld.game.entites.stations.SpinningWheelStation;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.MasonBenchRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.SpinningWheel_R;

public class SpeeningWheelItem extends PlaceableInventoryItem<SpeeningWheelItem> implements InventoryCraftingInterface {

    public SpeeningWheelItem() {
        this.name = "Spinning Wheel";
        this.desc = "Craft cloth items and clothing";
        this.spawnNames.add("spinningwheel");
        this.icon = InventoryDataRow.SpinningWheel;
        this.mesh = InventoryDataRow.SpinningWheel;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new SpinningWheelStation();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SpinningWheel_R();
    }
}
