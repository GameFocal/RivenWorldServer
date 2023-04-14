package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.DefenciveSpikedFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.DefenciveSpikedFencePlaceableRecipe;

public class DefenciveSpikedFencePlaceableItem extends PlaceableInventoryItem<DefenciveSpikedFencePlaceableItem> implements InventoryCraftingInterface {

    public DefenciveSpikedFencePlaceableItem() {
        this.name = "Defencive Spiked Wooden Fence";
        this.desc = "Defencive Fence to protect you in battle and injure your enemies";
        this.icon = InventoryDataRow.DefenciveSpikedFencePlaceable;
        this.mesh = InventoryDataRow.DefenciveSpikedFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("devencivespikedfence");
    }

    @Override
    public GameEntity spawnItem() {
        return new DefenciveSpikedFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DefenciveSpikedFencePlaceableRecipe();
    }
}
