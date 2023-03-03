package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.DoorPlaceableRecipe;

public class DoorPlaceableItem extends PlaceableInventoryItem<DoorPlaceableItem> implements InventoryCraftingInterface {

    public DoorPlaceableItem() {
        this.name = "DoorPlaceable";
        this.icon = InventoryDataRow.DoorPlaceable;
        this.mesh = InventoryDataRow.DoorPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DoorPlaceableRecipe();
    }
}
