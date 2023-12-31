package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.TentPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.TentPlaceableRecipe;

public class TentPlaceableItem extends PlaceableInventoryItem<TentPlaceableItem> implements InventoryCraftingInterface {

    public TentPlaceableItem() {
        this.name = "Tent";
        this.desc = "Stay out of the elements";
        this.icon = InventoryDataRow.TentPlaceable;
        this.mesh = InventoryDataRow.TentPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("tent");
    }

    @Override
    public GameEntity spawnItem() {
        return new TentPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TentPlaceableRecipe();
    }
}
