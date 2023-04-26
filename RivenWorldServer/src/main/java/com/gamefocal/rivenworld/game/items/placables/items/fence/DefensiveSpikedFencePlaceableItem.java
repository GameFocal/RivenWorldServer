package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.DefensiveSpikedFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.DefensiveSpikedFencePlaceableRecipe;

public class DefensiveSpikedFencePlaceableItem extends PlaceableInventoryItem<DefensiveSpikedFencePlaceableItem> implements InventoryCraftingInterface {

    public DefensiveSpikedFencePlaceableItem() {
        this.name = "Defensive Spiked Wooden Fence";
        this.desc = "Defensive Fence to protect you in battle";
        this.icon = InventoryDataRow.DefensiveSpikedFencePlaceable;
        this.mesh = InventoryDataRow.DefensiveSpikedFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("devensivespikedfence");
    }

    @Override
    public GameEntity spawnItem() {
        return new DefensiveSpikedFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DefensiveSpikedFencePlaceableRecipe();
    }
}
