package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.ChairPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.ChairPlaceableRecipe;

public class ChairPlaceableItem extends PlaceableInventoryItem<ChairPlaceableItem> implements InventoryCraftingInterface {

    public ChairPlaceableItem() {
        this.name = "Wooden Chair";
        this.desc = "A great addition to your house";
        this.mesh = InventoryDataRow.ChairPlaceable;
        this.icon = InventoryDataRow.ChairPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
        this.spawnNames.add("chair");
    }

    @Override
    public GameEntity spawnItem() {
        return new ChairPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChairPlaceableRecipe();
    }
}
