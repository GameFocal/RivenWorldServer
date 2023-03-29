package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.TallFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.TallFencePlaceableRecipe;

public class TallFencePlaceableItem extends PlaceableInventoryItem<TallFencePlaceableItem> implements InventoryCraftingInterface {

    public TallFencePlaceableItem() {
        this.name = "Tall Wooden Fence";
        this.desc = "A great decoration for your house";
        this.icon = InventoryDataRow.TallFencePlaceable;
        this.mesh = InventoryDataRow.TallFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new TallFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TallFencePlaceableRecipe();
    }
}
