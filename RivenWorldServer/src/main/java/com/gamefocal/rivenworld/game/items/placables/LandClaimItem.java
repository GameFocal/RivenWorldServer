package com.gamefocal.rivenworld.game.items.placables;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.items.placables.stations.CampFirePlaceableItem;
import com.gamefocal.rivenworld.game.recipes.placables.LandClaimPlaceableRecipe;

public class LandClaimItem extends PlaceableInventoryItem<CampFirePlaceableItem> implements InventoryCraftingInterface {

    public LandClaimItem() {
        this.name = "Land Claim";
        this.desc = "Use this to claim land and protect your buildings and items. You need to pay your taxes to keep your landclaim active";
        this.icon = InventoryDataRow.LandClaimPlaceable;
        this.mesh = InventoryDataRow.LandClaimPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("landclaim");
    }

    @Override
    public GameEntity spawnItem() {
        return new LandClaimEntity();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new LandClaimPlaceableRecipe();
    }
}
