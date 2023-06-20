package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.DefensiveFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.DefensiveFencePlaceableRecipe;

public class DefensiveFencePlaceableItem extends PlaceableInventoryItem<DefensiveFencePlaceableItem> implements InventoryCraftingInterface {

    public DefensiveFencePlaceableItem() {
        this.name = "Defensive Wooden Fence";
        this.desc = "A great Fence to help you in battle";
        this.icon = InventoryDataRow.DefensiveFencePlaceable;
        this.mesh = InventoryDataRow.DefensiveFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("defensivefence");
    }

    @Override
    public GameEntity spawnItem() {
        return new DefensiveFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DefensiveFencePlaceableRecipe();
    }
}
