package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.SmallSpikedFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.SmallSpikedFencePlaceableRecipe;

public class SmallSpikedFencePlaceableItem extends PlaceableInventoryItem<SmallSpikedFencePlaceableItem> implements InventoryCraftingInterface {

    public SmallSpikedFencePlaceableItem() {
        this.name = "Small Spiked Wooden Fence";
        this.desc = "Defencive Fence to protect you in battle and injure your enemies";
        this.icon = InventoryDataRow.SmallSpikedFencePlaceable;
        this.mesh = InventoryDataRow.SmallSpikedFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new SmallSpikedFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SmallSpikedFencePlaceableRecipe();
    }
}
