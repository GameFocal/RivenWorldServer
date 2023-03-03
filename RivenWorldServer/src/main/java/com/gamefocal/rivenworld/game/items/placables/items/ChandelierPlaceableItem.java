package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.lights.ChandelierPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.ChandelierPlaceableRecipe;

public class ChandelierPlaceableItem extends PlaceableInventoryItem<ChandelierPlaceableItem> implements InventoryCraftingInterface {

    public ChandelierPlaceableItem() {
        this.name = "ChandelierPlaceable";
        this.icon = InventoryDataRow.ChairPlaceable;
        this.mesh = InventoryDataRow.ChairPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 1;
    }

    @Override
    public GameEntity spawnItem() {
        return new ChandelierPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChandelierPlaceableRecipe();
    }
}
