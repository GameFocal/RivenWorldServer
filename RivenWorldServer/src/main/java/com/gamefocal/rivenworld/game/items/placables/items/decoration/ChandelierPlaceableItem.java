package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.lights.ChandelierPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.ChandelierPlaceableRecipe;

public class ChandelierPlaceableItem extends PlaceableInventoryItem<ChandelierPlaceableItem> implements InventoryCraftingInterface {

    public ChandelierPlaceableItem() {
        this.name = "Chandelier";
        this.desc = "Hangs from the ceiling and give light";
        this.icon = InventoryDataRow.ChandelierPlaceable;
        this.mesh = InventoryDataRow.ChandelierPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = false;
        this.placable.SnaptoBase = false;
        this.placable.BaseType = 1;
        this.spawnNames.add("chandelier");
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
