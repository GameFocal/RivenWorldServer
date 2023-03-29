package com.gamefocal.rivenworld.game.items.placables.items.doors;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.doors.DoorPlaceable5;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable5Item extends PlaceableInventoryItem<DoorPlaceable5Item> {

    public DoorPlaceable5Item() {
        this.name = "Basic Iron Door";
        this.desc = "A basic door forged from Iron";
        this.icon = InventoryDataRow.DoorPlaceable5;
        this.mesh = InventoryDataRow.DoorPlaceable5;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable5();
    }
}
