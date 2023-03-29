package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.FencePlaceable6;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.fence.FencePlaceable1Recipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.fence.FencePlaceable6Recipe;

public class FencePlaceable6Item extends PlaceableInventoryItem<FencePlaceable6Item> implements InventoryCraftingInterface {

    public FencePlaceable6Item() {
        this.name = "Wooden Fence";
        this.desc = "A great decoration for your house";
        this.icon = InventoryDataRow.FencePlaceable6;
        this.mesh = InventoryDataRow.FencePlaceable6;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new FencePlaceable6();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FencePlaceable6Recipe();
    }
}
