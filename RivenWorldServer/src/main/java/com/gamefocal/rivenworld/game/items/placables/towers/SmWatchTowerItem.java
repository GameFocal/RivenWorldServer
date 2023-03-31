package com.gamefocal.rivenworld.game.items.placables.towers;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.fence.TallFencePlaceable;
import com.gamefocal.rivenworld.game.entites.placable.towers.SmWatchTower;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.fence.TallFencePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.towers.SmWatchTower_R;

public class SmWatchTowerItem extends PlaceableInventoryItem<com.gamefocal.rivenworld.game.items.placables.items.fence.TallFencePlaceableItem> implements InventoryCraftingInterface {

    public SmWatchTowerItem() {
        this.name = "Small Watch Tower";
        this.desc = "A wooden watch tower";
        this.icon = InventoryDataRow.logwatch_tower2;
        this.mesh = InventoryDataRow.logwatch_tower2;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = false;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new SmWatchTower();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SmWatchTower_R();
    }
}
