package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.TallDefensiveSpikedFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.TallDefensiveSpikedFencePlaceableRecipe;

public class TallDefensiveSpikedFencePlaceableItem extends PlaceableInventoryItem<TallDefensiveSpikedFencePlaceableItem> implements InventoryCraftingInterface {

    public TallDefensiveSpikedFencePlaceableItem() {
        this.name = "Tall Spiked Wooden Fence";
        this.desc = "Defensive Fence to protect you in battle";
        this.icon = InventoryDataRow.TallDefensiveSpikedFencePlaceable;
        this.mesh = InventoryDataRow.TallDefensiveSpikedFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new TallDefensiveSpikedFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TallDefensiveSpikedFencePlaceableRecipe();
    }
}
