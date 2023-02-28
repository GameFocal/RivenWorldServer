package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.BedPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.BedPlaceableRecipe;

public class BedPlaceableItem extends PlaceableInventoryItem<BedPlaceableItem> implements InventoryCraftingInterface {
    public BedPlaceableItem() {
        this.icon = InventoryDataRow.bedPlaceable;
        this.mesh = InventoryDataRow.bedPlaceable;
    }

    @Override
    public GameEntity spawnItem() {
        return new BedPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new BedPlaceableRecipe();
    }
}
