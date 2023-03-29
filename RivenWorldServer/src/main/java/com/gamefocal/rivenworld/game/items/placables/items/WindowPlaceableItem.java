package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WindowPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.WindowPlaceableRecipe;

public class WindowPlaceableItem extends PlaceableInventoryItem<WindowPlaceableItem> implements InventoryCraftingInterface {

    public WindowPlaceableItem() {
        this.name = "Glass Window";
        this.icon = InventoryDataRow.WindowPlaceable;
        this.mesh = InventoryDataRow.WindowPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 0;
        this.placable.SnaptoBase = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new WindowPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WindowPlaceableRecipe();
    }
}
