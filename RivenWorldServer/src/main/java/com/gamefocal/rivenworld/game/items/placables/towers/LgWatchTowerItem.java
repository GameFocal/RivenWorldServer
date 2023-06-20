package com.gamefocal.rivenworld.game.items.placables.towers;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.TallFencePlaceable;
import com.gamefocal.rivenworld.game.entites.placable.towers.LgWatchTower;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.TallFencePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.towers.LgWatchTower_R;

public class LgWatchTowerItem extends PlaceableInventoryItem<com.gamefocal.rivenworld.game.items.placables.items.fence.TallFencePlaceableItem> implements InventoryCraftingInterface {

    public LgWatchTowerItem() {
        this.name = "Large Watch Tower";
        this.desc = "A wooden watch tower";
        this.icon = InventoryDataRow.logwatch_tower;
        this.mesh = InventoryDataRow.logwatch_tower;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
        this.spawnNames.add("logtower");
    }

    @Override
    public GameEntity spawnItem() {
        return new LgWatchTower();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LgWatchTower_R();
    }
}
