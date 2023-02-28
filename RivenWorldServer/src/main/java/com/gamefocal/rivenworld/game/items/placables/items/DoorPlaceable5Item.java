package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable5;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable5Item extends PlaceableInventoryItem<DoorPlaceable5Item> {

    public DoorPlaceable5Item() {
        this.icon = InventoryDataRow.DoorPlaceable5;
        this.mesh = InventoryDataRow.DoorPlaceable5;
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable5();
    }
}
