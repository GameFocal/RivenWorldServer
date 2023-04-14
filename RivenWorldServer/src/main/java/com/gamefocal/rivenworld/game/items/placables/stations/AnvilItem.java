package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.AnvilStation;
import com.gamefocal.rivenworld.game.entites.stations.SpinningWheelStation;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.Anvil_R;
import com.gamefocal.rivenworld.game.recipes.placables.SpinningWheel_R;

public class AnvilItem extends PlaceableInventoryItem<AnvilItem> implements InventoryCraftingInterface {

    public AnvilItem() {
        this.name = "Anvil";
        this.desc = "A device to create metal based products";
        this.spawnNames.add("anvil");
        this.icon = InventoryDataRow.Anvil;
        this.mesh = InventoryDataRow.Anvil;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
        this.spawnNames.add("anvil");
    }

    @Override
    public GameEntity spawnItem() {
        return new AnvilStation();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Anvil_R();
    }
}
