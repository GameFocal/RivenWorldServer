package com.gamefocal.rivenworld.game.items.placables.items.doors;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.doors.DoorPlaceable2;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable2Recipe;

public class DoorPlaceable2Item extends PlaceableInventoryItem<DoorPlaceable2Item> implements InventoryCraftingInterface {

    public DoorPlaceable2Item() {
        this.name = "Strong Wooden Door";
        this.desc = "A strong door made of wood and iron";
        this.icon = InventoryDataRow.DoorPlaceable2;
        this.mesh = InventoryDataRow.DoorPlaceable2;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
        this.spawnNames.add("strongwooddoor");
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable2();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DoorPlaceable2Recipe();
    }
}
