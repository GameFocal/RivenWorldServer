package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.TallDefenciveSpikedFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.fence.FencePlaceable1Recipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.fence.TallDefenciveSpikedFencePlaceableRecipe;

public class TallDefenciveSpikedFencePlaceableItem extends PlaceableInventoryItem<TallDefenciveSpikedFencePlaceableItem> implements InventoryCraftingInterface {

    public TallDefenciveSpikedFencePlaceableItem() {
        this.name = "Tall Spiked Wooden Fence";
        this.desc = "Defencive Fence to protect you in battle";
        this.icon = InventoryDataRow.TallDefenciveSpikedFencePlaceable;
        this.mesh = InventoryDataRow.TallDefenciveSpikedFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new TallDefenciveSpikedFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TallDefenciveSpikedFencePlaceableRecipe();
    }
}
