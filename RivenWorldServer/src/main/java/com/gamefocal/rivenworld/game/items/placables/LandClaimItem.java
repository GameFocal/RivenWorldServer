package com.gamefocal.rivenworld.game.items.placables;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.items.placables.items.CampFirePlaceableItem;

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
