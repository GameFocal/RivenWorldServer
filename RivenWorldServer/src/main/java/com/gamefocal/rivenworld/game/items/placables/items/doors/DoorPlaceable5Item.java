package com.gamefocal.rivenworld.game.items.placables.items.doors;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.doors.DoorPlaceable5;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable5Recipe;

public class DoorPlaceable5Item extends PlaceableInventoryItem<DoorPlaceable5Item> implements InventoryCraftingInterface {

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

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DoorPlaceable5Recipe();
    }
}
