package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.PassiveWaterWellStation;
import com.gamefocal.rivenworld.game.entites.stations.SpinningWheelStation;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.SpinningWheel_R;
import com.gamefocal.rivenworld.game.recipes.placables.WaterWellRecipe;

public class WaterWellItem extends PlaceableInventoryItem<SpeeningWheelItem> implements InventoryCraftingInterface {

    public WaterWellItem() {
        this.name = "Water Well";
        this.desc = "Collects passive drinking water for you";
        this.spawnNames.add("well");
        this.icon = InventoryDataRow.wellforwater;
        this.mesh = InventoryDataRow.wellforwater;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new PassiveWaterWellStation();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WaterWellRecipe();
    }
}
