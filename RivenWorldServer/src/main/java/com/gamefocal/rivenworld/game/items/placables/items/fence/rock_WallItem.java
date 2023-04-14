package com.gamefocal.rivenworld.game.items.placables.items.fence;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.rock_Wall;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.rock_WallRecipe;

public class rock_WallItem extends PlaceableInventoryItem<rock_WallItem> implements InventoryCraftingInterface {

    public rock_WallItem() {
        this.name = "Rock Wall";
        this.desc = "A wall made out of rocks";
        this.icon = InventoryDataRow.Rock_Wall;
        this.mesh = InventoryDataRow.Rock_Wall;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("rockwall");
    }

    @Override
    public GameEntity spawnItem() {
        return new rock_Wall();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new rock_WallRecipe();
    }
}
