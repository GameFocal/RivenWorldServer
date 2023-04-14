package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.DefenciveFencePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.DefenciveFencePlaceableRecipe;

public class DefenciveFencePlaceableItem extends PlaceableInventoryItem<DefenciveFencePlaceableItem> implements InventoryCraftingInterface {

    public DefenciveFencePlaceableItem() {
        this.name = "Defencive Wooden Fence";
        this.desc = "A great Fence to help you in battle";
        this.icon = InventoryDataRow.DefensiveFencePlaceable;
        this.mesh = InventoryDataRow.DefensiveFencePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("defencivefence");
    }

    @Override
    public GameEntity spawnItem() {
        return new DefenciveFencePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DefenciveFencePlaceableRecipe();
    }
}
