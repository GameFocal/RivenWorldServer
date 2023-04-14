package com.gamefocal.rivenworld.game.items.placables.items.doors;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.doors.DoorPlaceable3;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable3Recipe;

public class DoorPlaceable3Item extends PlaceableInventoryItem<DoorPlaceable3Item> implements InventoryCraftingInterface {

    public DoorPlaceable3Item() {
        this.name = "Cheap Wooden Door";
        this.desc = "This will keep the weather out but not your foes";
        this.icon = InventoryDataRow.DoorPlaceable3;
        this.mesh = InventoryDataRow.DoorPlaceable3;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
        this.spawnNames.add("cheapwooddoor");
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable3();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DoorPlaceable3Recipe();
    }
}
