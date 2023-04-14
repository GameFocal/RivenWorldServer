package com.gamefocal.rivenworld.game.items.placables.items.doors;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.doors.DoorPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceableRecipe;

public class DoorPlaceableItem extends PlaceableInventoryItem<DoorPlaceableItem> implements InventoryCraftingInterface {

    public DoorPlaceableItem() {
        this.name = "Basic Wooden Door";
        this.desc = "A door of iron and wood";
        this.icon = InventoryDataRow.DoorPlaceable;
        this.mesh = InventoryDataRow.DoorPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
        this.spawnNames.add("basicwooddoor");
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
