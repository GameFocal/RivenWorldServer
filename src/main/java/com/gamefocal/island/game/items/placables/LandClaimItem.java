package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.LandClaimEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.island.game.items.placables.items.CampFirePlaceableItem;

public class LandClaimItem extends PlaceableInventoryItem<CampFirePlaceableItem> {
    @Override
    public String slug() {
        return "LandClaimPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new LandClaimEntity();
    }
}
