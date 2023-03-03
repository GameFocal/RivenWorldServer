package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.RugPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.RugPlaceableRecipe;

public class RugPlaceableItem extends PlaceableInventoryItem<RugPlaceableItem> implements InventoryCraftingInterface {

    public RugPlaceableItem() {
        this.name = "RugPlaceable";
        this.icon = InventoryDataRow.RugPlaceable;
        this.mesh = InventoryDataRow.RugPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new RugPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new RugPlaceableRecipe();
    }
}
