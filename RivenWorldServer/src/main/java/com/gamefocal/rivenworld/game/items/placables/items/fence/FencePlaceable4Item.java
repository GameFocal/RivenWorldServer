package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.FencePlaceable4;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.FencePlaceable4Recipe;

public class FencePlaceable4Item extends PlaceableInventoryItem<FencePlaceable4Item> implements InventoryCraftingInterface {

    public FencePlaceable4Item() {
        this.name = "Wooden Fence";
        this.desc = "A great decoration for your house";
        this.icon = InventoryDataRow.FencePlaceable4;
        this.mesh = InventoryDataRow.FencePlaceable4;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new FencePlaceable4();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FencePlaceable4Recipe();
    }
}
