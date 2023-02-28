package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.CampFirePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.CampFirePlaceableRecipe;

public class CampFirePlaceableItem extends PlaceableInventoryItem<CampFirePlaceableItem> implements InventoryCraftingInterface {

    public CampFirePlaceableItem() {
        this.icon = InventoryDataRow.CampfirePlaceable;
        this.mesh = InventoryDataRow.CampfirePlaceable;
    }

    @Override
    public GameEntity spawnItem() {
        return new CampFirePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CampFirePlaceableRecipe();
    }
}
